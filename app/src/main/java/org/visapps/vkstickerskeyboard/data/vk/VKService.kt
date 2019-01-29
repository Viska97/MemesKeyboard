package org.visapps.vkstickerskeyboard.data.vk

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface VKService {

    @GET("messages.getConversations")
    suspend fun getConversations(@Query("v") v : String,
                         @Query("access_token") access_token : String,
                         @Query("count") count : Int,
                         @Query("filter") filter : String,
                         @Query("extended") extended : Boolean,
                         @Query("fields") fields : String)
            : Response<ConversationsResponse>
}