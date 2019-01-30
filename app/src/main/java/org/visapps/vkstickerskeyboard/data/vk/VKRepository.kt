package org.visapps.vkstickerskeyboard.data.vk

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKSdk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.visapps.vkstickerskeyboard.data.response.Result
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class VKRepository private constructor(private val vkservice : VKService){

    companion object {

        private const val URL = "https://api.vk.com/method/"

        private const val API_VERSION = "5.92"


        @Volatile private var instance: VKRepository? = null

        fun get() =
            instance ?: synchronized(this) {
                instance ?: buildRepository().also { instance = it }
            }

        private fun buildRepository() : VKRepository{
            return VKRepository(buildService())
        }

        private fun buildService() : VKService{
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

    suspend fun getChats() : Result<ConversationsResponse> {
        val response = vkservice.getConversations(
                API_VERSION,
                VKAccessToken.currentToken().accessToken,
                20,
                "all",
                true,
                "photo_100").await()
        try {
            if (response.isSuccessful) {
                return Result.Success(response.body())
            } else {
                return Result.Error(IOException("Error occurred during fetching movies!"))
            }
        } catch (e: Exception) {
            return Result.Error(IOException("Unable to fetch movies!"))
        }
    }


}