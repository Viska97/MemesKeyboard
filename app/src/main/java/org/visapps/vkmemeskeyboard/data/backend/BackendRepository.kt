package org.visapps.vkmemeskeyboard.data.backend

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.coroutineScope
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.visapps.vkmemeskeyboard.data.database.AppDatabase
import org.visapps.vkmemeskeyboard.data.models.Meme
import org.visapps.vkmemeskeyboard.data.models.Pack
import org.visapps.vkmemeskeyboard.data.models.PackStatus
import org.visapps.vkmemeskeyboard.util.Result
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class BackendRepository(private val dataSource: BackendDataSource, private val database: AppDatabase) {

    suspend fun getPackStatus(packId: Int) : Int = coroutineScope {
        val status = database.packDao().getPackStatusById(packId)
        status?.let {
            return@coroutineScope it
        }
        return@coroutineScope PackStatus.NOTSAVED
    }

    suspend fun getPack(packId: Int) : Result<Pack> = coroutineScope {
        val result = database.packDao().getPackById(packId)
        result?.let {
            return@coroutineScope Result.Success(it)
        }
        return@coroutineScope Result.Error(IOException("No pack found"))
    }

    suspend fun updatePackStatus(packId: Int, status : Int) = coroutineScope {
        database.packDao().updatePackStatus(packId, status)
    }

    suspend fun savePack(packId : Int, memes : List<Meme>) = coroutineScope {
        database.runInTransaction {
            database.stickerDao().insertMemes(memes)
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

    suspend fun getMemes(packId : Int,
                         forceNetwork : Boolean = false)
            : Result<List<Meme>> = coroutineScope {
        if(forceNetwork){
            return@coroutineScope dataSource.getMemes(packId)
        }
        else{
            val result = database.stickerDao().getMemes(packId)
            if(result.isEmpty()){
                dataSource.getMemes(packId)
            }
            return@coroutineScope Result.Success(result)
        }
    }

    suspend fun loadPacks(searchText: String,
                            pageSize: Int) : Result<List<Pack>> = coroutineScope {
        val offset = database.packDao().updatedPacksCount()
        val result = dataSource.searchPacks(searchText, pageSize, offset)
        if (result is Result.Success) {
            database.runInTransaction {
                database.packDao().insertPacks(result.data)
                database.packDao().updateSavedPacks(result.data.map { it.id })
            }
        }
        return@coroutineScope result
    }

    suspend fun refreshPacks(searchText: String,
                             pageSize: Int) : Result<List<Pack>> = coroutineScope {
        val result = dataSource.searchPacks(searchText, pageSize, 0)
        if(result is Result.Success) {
            database.runInTransaction {
                database.packDao().deleteNotSavedPacks()
                database.packDao().invalidatePacks()
                database.packDao().insertPacks(result.data)
                database.packDao().updateSavedPacks(result.data.map { it.id })
            }
        }
        return@coroutineScope result
    }

    fun getPacks(searchText: String,
                 pageSize: Int,
                 prefetchDistance : Int,
                 boundaryCallback: PagedList.BoundaryCallback<Pack>) : LiveData<PagedList<Pack>> {
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(pageSize)
            .setPrefetchDistance(prefetchDistance)
            .build()
        return database.packDao().searchPacks("%$searchText%")
            .toLiveData(config = config, boundaryCallback = boundaryCallback)
    }

    fun getSavedPacks() : LiveData<List<Pack>> {
        return database.packDao().getSavedPacks()
    }

    companion object {

        private const val URL = "http://92.53.67.78/"

        @Volatile
        private var instance: BackendRepository? = null

        fun get(context: Context) =
            instance ?: synchronized(this) {
                instance
                    ?: buildRepository(context).also { instance = it }
            }

        private fun buildRepository(context: Context): BackendRepository {
            return BackendRepository(
                BackendDataSource(
                    buildService()
                ), AppDatabase.get(context)
            )
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