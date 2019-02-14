package org.visapps.vkstickerskeyboard.data.vk

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKSdk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.visapps.vkstickerskeyboard.data.database.AppDatabase
import org.visapps.vkstickerskeyboard.data.models.Chat
import org.visapps.vkstickerskeyboard.data.models.Dialog
import org.visapps.vkstickerskeyboard.util.ListStatus
import org.visapps.vkstickerskeyboard.util.NetworkState
import org.visapps.vkstickerskeyboard.util.Result
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.Exception
import java.util.*

class VKRepository private constructor(private val dataSource : VKDataSource, private val database : AppDatabase) {

    val authStatus = MutableLiveData<Boolean>()

    init{
        authStatus.postValue(VKSdk.isLoggedIn())
    }

    fun getDialogs(pageSize: Int) : ListStatus<Dialog> {
        val boundaryCallback = DialogsBoundaryCallback(pageSize, dataSource, database, this::handleLogout)
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            refreshDialogs(pageSize)
        }
        val config = PagedList.Config.Builder().setPageSize(pageSize).setEnablePlaceholders(false)
            .setInitialLoadSizeHint(pageSize).setPrefetchDistance(5).build()
        val livePagedList = database.dialogDao().getDialogs()
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

    private fun refreshDialogs(pageSize: Int) : LiveData<Int> {
        val networkState = MutableLiveData<Int>()
        networkState.postValue(NetworkState.RUNNING)
        GlobalScope.launch(Dispatchers.IO) {
            networkState.postValue(NetworkState.RUNNING)
            val result = dataSource.getConversations(API_VERSION,VKAccessToken.currentToken().accessToken,pageSize,"all", true, null, "photo_max")
            when(result){
                is Result.Success -> {
                    database.runInTransaction{
                        database.dialogDao().deleteDialogs()
                        database.dialogDao().insertDialogs(processDialogs(result.data))
                    }
                }
                is Result.Error -> {networkState.postValue(NetworkState.FAILED)}
                is Result.NotAuthenticated -> {handleLogout()}
            }
        }
        return networkState
    }

    private fun handleLogout() {
        authStatus.postValue(false)
        VKSdk.logout()
    }

    companion object {

        private const val URL = "https://api.vk.com/method/"

        const val API_VERSION = "5.92"


        @Volatile
        private var instance: VKRepository? = null

        fun get(context: Context) =
            instance ?: synchronized(this) {
                instance ?: buildRepository(context).also { instance = it }
            }

        private fun buildRepository(context: Context): VKRepository {
            return VKRepository(VKDataSource(buildService()), AppDatabase.get(context))
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

        fun processDialogs(conversations : ConversationsResponse) : List<Dialog> {
            val result = ArrayList<Dialog>()
            conversations.response?.items?.forEach {item ->
                val peerId = item.conversation?.peer?.id
                val last_message_id = item.conversation?.lastMessageId
                val allowed = item.conversation?.canWrite?.allowed
                var name : String? = null
                var photo : String? = null
                when(item.conversation?.peer?.type){
                    "user" -> {

                    }
                    "group" -> {

                    }
                    "chat" -> {

                    }
                }
                if(listOf(peerId,last_message_id,allowed,name,photo).any { it !=null }){
                    result.add(Dialog(peerId!!,last_message_id!!,name!!, allowed!!,photo!!))
                }
            }
            return result
        }
    }

}