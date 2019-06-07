package org.visapps.vkmemeskeyboard.data.vk

import org.visapps.vkmemeskeyboard.util.Result
import retrofit2.Response
import java.io.IOException

fun <T : Any> Response<out VKResponse<T>>.successful() : Boolean {
    if (isSuccessful && body()?.error == null && body()?.response != null){
        return true
    }
    return false
}

fun <T1 : Any, T2 : Any> processUnsuccessfulResponse (response : Response<out VKResponse<T1>>) : Result<T2> {
    if(response.isSuccessful && response.body()?.error?.body?.errorCode in arrayOf(5,18)){
        return Result.NotAuthenticated(response.body()?.error?.body?.errorMsg ?: "")
    }
    else if(response.isSuccessful && response.body()?.error != null){
        return Result.Error(VKException(
            response.body()?.error?.body?.errorCode?: 0,
            response.body()?.error?.body?.errorMsg?: ""))
    }
    return Result.Error(IOException("Network error"))
}

class VKException(val errorCode : Int, private val errorMsg : String) : Exception(errorMsg)