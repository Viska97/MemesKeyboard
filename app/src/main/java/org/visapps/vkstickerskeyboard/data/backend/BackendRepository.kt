package org.visapps.vkstickerskeyboard.data.backend

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.visapps.vkstickerskeyboard.data.database.AppDatabase
import org.visapps.vkstickerskeyboard.data.models.Pack
import org.visapps.vkstickerskeyboard.data.models.PackStatus
import org.visapps.vkstickerskeyboard.data.models.Sticker
import org.visapps.vkstickerskeyboard.util.ListStatus
import org.visapps.vkstickerskeyboard.util.NetworkState
import org.visapps.vkstickerskeyboard.util.Result
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class BackendRepository(private val datasource: BackendDataSource, private val database: AppDatabase) {

    fun getPackStatus(packId: Int) : Int {
        val status = database.packDao().getPackStatusById(packId)
        status?.let {
            return it
        }
        return PackStatus.NOTSAVED
    }

    suspend fun getPack(packId: Int) : Result<Pack> {
        val result = withContext(Dispatchers.IO) { database.packDao().getPackById(packId)}
        result?.let {
            return Result.Success(it)
        }
        return Result.Error(IOException("No pack found"))
    }

    suspend fun updatePackStatus(packId: Int, status : Int) = coroutineScope {
        database.packDao().updatePackStatus(packId, status)
    }

    suspend fun savePack(packId : Int, stickers : List<Sticker>) = coroutineScope {
        database.runInTransaction {
            database.stickerDao().insertStickers(stickers)
            database.packDao().updatePackStatus(packId, PackStatus.SAVED)
        }
    }

    suspend fun removePack(packId: Int) = coroutineScope {
        database.runInTransaction {
            val pack = database.packDao().getPackById(packId)
            pack?.let {
                if(it.updated){
                    database.packDao().deletePackById(it.id)
                }
                else{
                    database.packDao().updatePackStatus(it.id, PackStatus.NOTSAVED)
                }
            }
        }
    }

    suspend fun getStickers(packId : Int, forceNetwork : Boolean = false) : Result<List<Sticker>> {
        if(forceNetwork){
            return datasource.getStickers(packId)
        }
        else{
            val result = withContext(Dispatchers.IO){database.stickerDao().getStickers(packId)}
            if(result.isEmpty()){
                return datasource.getStickers(packId)
            }
            return Result.Success(result)
        }
    }

    fun getSavedPacks() : LiveData<List<Pack>> {
        return database.packDao().getSavedPacks()
    }

    fun searchPacks(searchText: String, pageSize: Int): ListStatus<Pack> {
        val boundaryCallback = PacksBoundaryCallback(searchText, pageSize, datasource, database)
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            refresh(searchText, pageSize)
        }
        val config = PagedList.Config.Builder().setPageSize(pageSize).setEnablePlaceholders(false)
            .setInitialLoadSizeHint(pageSize).setPrefetchDistance(5).build()
        val livePagedList = database.packDao().searchPacks("%$searchText%")
            .toLiveData(config = config, boundaryCallback = boundaryCallback)
        return ListStatus(
            pagedList = livePagedList,
            networkState = boundaryCallback.networkState,
            retry = {
                boundaryCallback.reload()
            },
            refresh = {
                refreshTrigger.value = null
            },
            refreshState = refreshState
        )
    }

    private fun refresh(searchText: String, pageSize: Int): LiveData<Int> {
        val networkState = MutableLiveData<Int>()
        networkState.postValue(NetworkState.RUNNING)
        GlobalScope.launch(Dispatchers.IO) {
            networkState.postValue(NetworkState.RUNNING)
            val result = datasource.searchPacks(searchText, pageSize, 0)
            when (result) {
                is Result.Success -> {
                    database.runInTransaction {
                        database.packDao().deleteNotSavedPacks()
                        database.packDao().invalidatePacks()
                        database.packDao().insertPacks(result.data)
                        database.packDao().updateSavedPacks(result.data.map { it.id })
                    }
                    networkState.postValue(NetworkState.SUCCESS)
                }
                is Result.Error -> {
                    networkState.postValue(NetworkState.FAILED)
                }
            }
        }
        return networkState
    }

    companion object {

        private const val URL = "http://92.53.67.78/"

        @Volatile
        private var instance: BackendRepository? = null

        fun get(context: Context) =
            instance ?: synchronized(this) {
                instance ?: buildRepository(context).also { instance = it }
            }

        private fun buildRepository(context: Context): BackendRepository {
            return BackendRepository(BackendDataSource(buildService()), AppDatabase.get(context))
        }

        private fun buildService(): BackendService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            return Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BackendService::class.java)
        }
    }
}