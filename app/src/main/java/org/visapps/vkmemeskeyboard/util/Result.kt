package org.visapps.vkmemeskeyboard.util

sealed class Result<out T: Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    data class NotAuthenticated(val message : String) : Result<Nothing>()
}