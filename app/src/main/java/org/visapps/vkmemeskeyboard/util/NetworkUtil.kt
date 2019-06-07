package org.visapps.vkmemeskeyboard.util

import java.io.IOException

suspend fun <T : Any> safeApiCall(call: suspend () -> Result<T>): Result<T> {
    return try {
        call()
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(IOException(e))
    }
}

enum class NetworkState{
    RUNNING,
    SUCCESS,
    FAILED
}