package org.visapps.vkstickerskeyboard.data.vk

import android.util.Log
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKSdk
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.visapps.vkstickerskeyboard.data.models.Chat
import org.visapps.vkstickerskeyboard.data.response.Result
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.Exception

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
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(VKService::class.java)
        }
    }

    suspend fun getChats() : Result<List<Chat>>{
        try{
            val response = vkservice.getConversations(API_VERSION,VKAccessToken.currentToken().accessToken,20,"all",true,"photo_100")
            if (response.isSuccessful){
                val chats = response.body()?.response?.profiles?.map { Chat(it.id, it.photo100) };
                return Result.Success(chats)
            }
            else{
                return Result.Error(IOException("Error occurred during fetching movies!"))
            }
        }
        catch(e : Exception){
            return Result.Error(IOException("Error occurred during fetching movies!"))
        }
    }


}