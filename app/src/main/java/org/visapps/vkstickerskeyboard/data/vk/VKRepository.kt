package org.visapps.vkstickerskeyboard.data.vk

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.vk.sdk.VKSdk
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
            return Retrofit.Builder()
                .baseUrl(URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(VKService::class.java)
        }
    }

    suspend fun getChats() : Result<ConversationsResponse>{
        val response = vkservice.getConversations(API_VERSION,VKSdk.getAccessToken().accessToken,20,"all",true,"photo_100").await()
        try {
            if (response.isSuccessful)
                return Result.Success(response.body())
            return Result.Error(IOException("Error occurred during fetching movies!"))
        } catch(e : Exception){
            return Result.Error(IOException("Unable to fetch movies!"))
        }
    }


}