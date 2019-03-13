package org.visapps.vkmemeskeyboard.data.vk

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VKService {

    @GET("messages.getConversations")
    fun getConversationsAsync(@Query("v") v : String,
                              @Query("access_token") access_token : String,
                              @Query("count") count : Int,
                              @Query("filter") filter : String,
                              @Query("extended") extended : Boolean,
                              @Query("start_message_id") start_message_id : Int?,
                              @Query("fields") fields : String)
            : Deferred<Response<ConversationsResponse>>
}