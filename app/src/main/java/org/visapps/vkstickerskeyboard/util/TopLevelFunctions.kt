package org.visapps.vkstickerskeyboard.util

import org.visapps.vkstickerskeyboard.data.response.Result
import java.io.IOException

suspend fun <T : Any> safeApiCall(call: suspend () -> Result<T>, errorMessage: String): Result<T> = try {
    call.invoke()
} catch (e: Exception) {
    Result.Error(IOException(errorMessage, e))
}