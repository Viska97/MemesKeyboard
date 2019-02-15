package org.visapps.vkstickerskeyboard.util

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.paging.*
import java.io.IOException
import android.util.DisplayMetrics
import android.util.Log


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

fun calculateNoOfColumns(context: Context): Int {
    val displayMetrics = context.resources.displayMetrics
    val dpWidth = displayMetrics.widthPixels / displayMetrics.density
    return (dpWidth / 120).toInt()
}


fun toVisibility(constraint : Boolean): Int {
    return if (constraint) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun switchVisibility(constraint : Boolean): Int {
    return if (constraint) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

sealed class Result<out T: Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    class NotAuthenticated() : Result<Nothing>()
}

class NotAuthenticatedException() : Exception()

object NetworkState{
    const val RUNNING = 0
    const val SUCCESS = 1
    const val FAILED = 2
}

object KeyboardState{
    const val NOTAUTHENTICATED = 0
    const val DIALOGS = 1
    const val STICKERS = 2
}
