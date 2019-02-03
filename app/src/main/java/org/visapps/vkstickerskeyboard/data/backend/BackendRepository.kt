package org.visapps.vkstickerskeyboard.data.backend

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.visapps.vkstickerskeyboard.data.database.AppDatabase
import org.visapps.vkstickerskeyboard.data.models.Pack
import org.visapps.vkstickerskeyboard.util.ListStatus
import org.visapps.vkstickerskeyboard.util.NetworkState
import org.visapps.vkstickerskeyboard.util.Result
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BackendRepository(private val datasource : BackendDataSource, private val database : AppDatabase) {

    fun searchPacks(searchText : String, pageSize: Int) : ListStatus<Pack> {
        val job = Job()
        val boundaryCallback = PacksBoundaryCallback(searchText,pageSize,datasource,database, job)
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            refresh(searchText, pageSize)
        }
        val livePagedList = database.packDao().searchPacks(searchText).toLiveData(pageSize = pageSize, boundaryCallback = boundaryCallback)
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

    private fun refresh(searchText : String, pageSize: Int) : LiveData<Int> {
        val networkState = MutableLiveData<Int>()
        networkState.postValue(NetworkState.RUNNING)
        GlobalScope.launch(Dispatchers.IO) {
            networkState.postValue(NetworkState.RUNNING)
            val result = datasource.searchPacks(searchText, pageSize, 0)
            when(result){
                is Result.Success -> {
                    database.runInTransaction {
                        database.packDao().deleteNotSaved()
                        database.packDao().insertPacks(result.data)
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