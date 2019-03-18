package org.visapps.vkmemeskeyboard.data.vk

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKSdk
import kotlinx.coroutines.coroutineScope
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.visapps.vkmemeskeyboard.data.database.AppDatabase
import org.visapps.vkmemeskeyboard.data.models.Dialog
import org.visapps.vkmemeskeyboard.data.models.User
import org.visapps.vkmemeskeyboard.util.Result
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VKRepository private constructor(private val dataSource : VKDataSource, private val database : AppDatabase) {

    val isLoggedIn = MutableLiveData<Boolean>()

    init{
        isLoggedIn.postValue(VKSdk.isLoggedIn())
    }

    suspend fun loadUser() : Result<User> = coroutineScope {
        val result : Result<User> = dataSource.getCurrentUser(API_VERSION, VKAccessToken.currentToken().accessToken, "photo_max", "Num")
        when(result) {
            is Result.Success ->{
                database.userDao().insertUser(result.data)
            }
            is Result.NotAuthenticated -> {
                handleLogout()
            }
            is Result.Error -> {
                database.userDao().getUser()?.let {
                    return@coroutineScope Result.Success(it)
                }
            }
        }
        return@coroutineScope result
    }

    suspend fun loadDialogs(pageSize: Int,
                            startMessageId: Int?) : Result<List<Dialog>> = coroutineScope {
        val result = dataSource.getDialogs(
            VKRepository.API_VERSION,
            VKAccessToken.currentToken().accessToken,
            pageSize,
            "all",
            true,
            startMessageId,
            "photo_max"
        )
        if(result is Result.Success){
            database.dialogDao().insertDialogs(result.data)
        }
        return@coroutineScope result
    }

    suspend fun refreshDialogs(pageSize: Int) : Result<List<Dialog>> = coroutineScope {
        val result = dataSource.getDialogs(
            API_VERSION,
            VKAccessToken.currentToken().accessToken,
            pageSize,
            "all",
            true,
            null,
            "photo_max")
        if(result is Result.Success){
            database.runInTransaction{
                database.dialogDao().deleteDialogs()
                database.dialogDao().insertDialogs(result.data)
            }
        }
        return@coroutineScope result
    }

    fun getDialogs(pageSize: Int,
                   prefetchDistance : Int,
                   boundaryCallback: PagedList.BoundaryCallback<Dialog>) : LiveData<PagedList<Dialog>> {
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(pageSize)
            .setPrefetchDistance(prefetchDistance)
            .build()
        Log.i(tag, "returning list")
        return database.dialogDao().getDialogs()
            .toLiveData(config = config, boundaryCallback = boundaryCallback)
    }

    fun handleLogIn() {
        isLoggedIn.postValue(VKSdk.isLoggedIn())
    }

    private fun handleLogout() {
        isLoggedIn.postValue(false)
        VKSdk.logout()
    }

    companion object {

        private const val URL = "https://api.vk.com/method/"

        const val API_VERSION = "5.92"

        const val tag = "VKRep"


        @Volatile
        private var instance: VKRepository? = null

        fun get(context: Context) =
            instance ?: synchronized(this) {
                instance
                    ?: buildRepository(context).also { instance = it }
            }

        private fun buildRepository(context: Context): VKRepository {
            return VKRepository(
                VKDataSource(buildService()),
                AppDatabase.get(context)
            )
        }

        private fun buildService(): VKService {
            val interceptor = HttpLoggingInterceptor();
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            return Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(VKService::class.java)
        }
    }

}