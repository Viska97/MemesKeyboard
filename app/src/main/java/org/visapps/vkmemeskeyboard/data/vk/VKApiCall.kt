package org.visapps.vkmemeskeyboard.data.vk

import org.visapps.vkmemeskeyboard.util.Result
import retrofit2.Response
import java.io.IOException

suspend fun <T : Any> vkApiCall(call: suspend () -> Response<out VKResponse<T>>): Result<T> {
    return try {
        val response = call()
        if (response.isSuccessful && response.body()?.error == null){
            response.body()?.response?.let {
                return Result.Success(it)
            }
        }
        else if(response.isSuccessful && response.body()?.error?.body?.errorCode in arrayOf(5,18)){
            return Result.NotAuthenticated(response.body()?.error?.body?.errorMsg ?: "")
        }
        else if(response.isSuccessful && response.body()?.error != null){
            return Result.Error(VKException(
                response.body()?.error?.body?.errorCode?: 0,
                response.body()?.error?.body?.errorMsg?: ""))
        }
        return Result.Error(IOException("Network error"))
    } catch (e: Exception) {
        Result.Error(IOException(e))
    }
}

class VKException(val errorCode : Int, private val errorMsg : String) : Exception(errorMsg)