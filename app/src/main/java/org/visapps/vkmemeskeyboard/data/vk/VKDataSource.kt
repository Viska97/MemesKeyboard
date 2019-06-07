package org.visapps.vkmemeskeyboard.data.vk

import org.visapps.vkmemeskeyboard.data.models.Dialog
import org.visapps.vkmemeskeyboard.data.models.User
import org.visapps.vkmemeskeyboard.util.Result
import org.visapps.vkmemeskeyboard.util.safeApiCall
import java.io.IOException
import java.util.ArrayList

class VKDataSource(private val vkService: VKService) {

    suspend fun getCurrentUser(v : String,
                               access_token : String,
                               fields : String,
                               name_case : String) : Result<User> = safeApiCall {
        val response = vkService.getUsersAsync(v,access_token, null, fields, name_case).await()
        if(response.successful()) {
            response.body()?.response?.users?.let {
                return@safeApiCall Result.Success(it[0])
            }
        }
        return@safeApiCall processUnsuccessfulResponse<UsersPage, User>(response)
    }

    suspend fun getDialogs(v : String,
                           access_token : String,
                           count : Int,
                           filter : String,
                           extended : Boolean,
                           start_message_id : Int?,
                           fields : String) : Result<List<Dialog>> = safeApiCall {
        val response = vkService.getConversationsAsync(v,access_token,count,filter,extended,start_message_id,fields).await()
        if(response.isSuccessful){
            response.body()?.let {
                return@safeApiCall Result.Success(processDialogs(it))
            }
        }
        return@safeApiCall Result.Error(
            IOException("Network error")
        )
    }

    private fun processDialogs(conversations : ConversationsResponse) : List<Dialog> {
        val result = ArrayList<Dialog>()
        conversations.response?.items?.forEach {item ->
            val peerId = item.conversation?.peer?.id
            val localId = item.conversation?.peer?.localId
            val lastMessageId = item.conversation?.lastMessageId
            val allowed = item.conversation?.canWrite?.allowed
            var name : String? = null
            var photo : String? = null
            when(item.conversation?.peer?.type){
                "user" -> {
                    val profile = conversations.response?.profiles?.find { it.id == peerId}
                    name = profile?.firstName + profile?.lastName
                    photo = profile?.photomax
                }
                "group" -> {
                    val group = conversations.response?.groups?.find { it.id == localId }
                    name = group?.name
                    photo = group?.photo_max
                }
                "chat" -> {
                    name = item.conversation?.chatSettings?.title
                    photo = item.conversation?.chatSettings?.photo?.photo_200
                }
            }
            if(listOf(peerId,lastMessageId,allowed,name,photo).any { it !=null }){
                result.add(
                    Dialog(
                        peerId ?: -1,
                        lastMessageId ?: -1,
                        name ?: "",
                        allowed ?: false,
                        photo ?: ""
                    )
                )
            }
        }
        return result
    }

}