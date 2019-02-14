package org.visapps.vkstickerskeyboard.ui.keyboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import com.vk.sdk.VKSdk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.visapps.vkstickerskeyboard.util.SingleLiveEvent
import org.visapps.vkstickerskeyboard.data.models.Chat
import org.visapps.vkstickerskeyboard.data.vk.VKRepository
import org.visapps.vkstickerskeyboard.util.Result
import kotlin.coroutines.CoroutineContext

class StickersKeyboardViewModel(private val repository : VKRepository) : ViewModel(), CoroutineScope {

    companion object {
        object KeyboardState{
            const val NOTAUTHENTICATED = 0
            const val DIALOGS = 1
            const val STICKERS = 2
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    val authStatus = repository.authStatus
    val status = repository.getDialogs(20)

    val keyboardState = MediatorLiveData<Int>()
    val dialogs = status.pagedList
    val networkState = status.networkState
    val refreshState = status.refreshState

    val loginStatus = MutableLiveData<Boolean>()
    val loadingState = MutableLiveData<Boolean>()
    val chats = MutableLiveData<List<Chat>>()
    val error = SingleLiveEvent<String>()

    private var job = Job()

    init{
        keyboardState.addSource(authStatus) {setState(it)}
    }

    fun keyboardState() : LiveData<Int> {
        val state =
    }

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }

    fun loadState() {
        loginStatus.postValue(VKSdk.isLoggedIn())
        if(VKSdk.isLoggedIn()){
            loadChats()
        }
    }

    fun loadChats() {
        this.launch(context = coroutineContext) {
            chats.postValue(null)
            loadingState.postValue(true)
            val result = VKRepository.get().getChats()
            when(result){
                is Result.Success -> chats.postValue(result.data)
                is Result.Error -> error.postValue("Network error")
            }
            loadingState.postValue(false)
        }

    }

    private fun setState(isLoggedIn : Boolean) {
        val currentState = keyboardState.value
        if(isLoggedIn && currentState == KeyboardState.NOTAUTHENTICATED){
            keyboardState.postValue(KeyboardState.DIALOGS)
        }
    }

    fun reload() {
        status.retry.invoke()
    }
}