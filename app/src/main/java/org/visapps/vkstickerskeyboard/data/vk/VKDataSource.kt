package org.visapps.vkstickerskeyboard.data.vk

import org.visapps.vkstickerskeyboard.util.NotAuthenticatedException
import org.visapps.vkstickerskeyboard.util.Result
import org.visapps.vkstickerskeyboard.util.safeApiCall
import java.io.IOException

class VKDataSource(private val vkService: VKService) {

    suspend fun getConversations(v : String,
                                 access_token : String,
                                 count : Int,
                                 filter : String,
                                 extended : Boolean,
                                 start_message_id : Int?,
                                 fields : String) = safeApiCall { requestConversations(v,access_token,count,filter,extended,start_message_id,fields) }

    private suspend fun requestConversations(v : String,
                                             access_token : String,
                                             count : Int,
                                             filter : String,
                                             extended : Boolean,
                                             start_message_id : Int?,
                                             fields : String) : Result<ConversationsResponse> {
        val response = vkService.getConversations(v,access_token,count,filter,extended,start_message_id,fields).await()
        if(response.isSuccessful){
            response.body()?.let {
                return Result.Success(it)
            }
        }
        else if(response.code() == 401){
            return  Result.NotAuthenticated()
        }
        return Result.Error(
            IOException("Network error")
        )
    }

}