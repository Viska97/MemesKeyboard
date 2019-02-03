package org.visapps.vkstickerskeyboard.util

import android.view.View
import androidx.lifecycle.LiveData
import androidx.paging.*
import java.io.IOException

data class ListStatus<T>(
    val pagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<Int>,
    val refreshState: LiveData<Int>,
    val refresh: () -> Unit,
    val retry: () -> Unit)

suspend fun <T : Any> safeApiCall(call: suspend () -> Result<T>): Result<T> {
    return try {
        call()
    } catch (e: Exception) {
        Result.Error(IOException(e))
    }
}

fun toVisibility(constraint : Boolean): Int {
    return if (constraint) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

sealed class Result<out T: Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

object NetworkState{
    const val RUNNING = 0
    const val SUCCESS = 1
    const val FAILED = 2
}
