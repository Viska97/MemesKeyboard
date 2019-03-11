package org.visapps.vkmemeskeyboard.ui.keyboard

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.visapps.vkmemeskeyboard.data.backend.BackendRepository
import org.visapps.vkmemeskeyboard.data.vk.VKRepository
import org.visapps.vkmemeskeyboard.util.KeyboardState
import kotlin.coroutines.CoroutineContext

class MemesKeyboardViewModel(backendRepository: BackendRepository, vkRepository : VKRepository)
    : ViewModel(), CoroutineScope {

    private val TAG = "SKViewModel"

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private val authStatus = vkRepository.authStatus
    val status = vkRepository.getDialogs(20)

    val keyboardState = MediatorLiveData<Int>()
    val dialogs = status.pagedList
    val networkState = status.networkState
    val refreshState = status.refreshState

    private var job = Job()

    init{
        keyboardState.addSource(authStatus) {updateState(it)}
    }

    public override fun onCleared() {
        job.cancel()
        Log.i(TAG, "On cleared")
        super.onCleared()
    }

    private fun updateState(isLoggedIn : Boolean) {
        val currentState = keyboardState.value
        if(isLoggedIn && (currentState == KeyboardState.NOTAUTHENTICATED || currentState == null)){
            keyboardState.postValue(KeyboardState.DIALOGS)
        }
        else if(!isLoggedIn && (currentState == KeyboardState.DIALOGS || currentState == KeyboardState.STICKERS)){
            keyboardState.postValue(KeyboardState.NOTAUTHENTICATED)
        }
    }

    fun refreshDialogs() {
        status.refresh.invoke()
    }

    fun reloadDialogs() {
        status.retry.invoke()
    }

}