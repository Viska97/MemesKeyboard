package org.visapps.vkstickerskeyboard.ui.keyboard

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.visapps.vkstickerskeyboard.data.backend.BackendRepository
import org.visapps.vkstickerskeyboard.data.vk.VKRepository
import org.visapps.vkstickerskeyboard.util.KeyboardState
import kotlin.coroutines.CoroutineContext

class StickersKeyboardViewModel(backendRepository: BackendRepository, vkRepository : VKRepository)
    : ViewModel(), CoroutineScope {

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
        keyboardState.postValue(KeyboardState.NOTAUTHENTICATED)
        keyboardState.addSource(authStatus) {updateState(it)}
    }

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }

    private fun updateState(isLoggedIn : Boolean) {
        val currentState = keyboardState.value
        if(isLoggedIn && currentState == KeyboardState.NOTAUTHENTICATED){
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