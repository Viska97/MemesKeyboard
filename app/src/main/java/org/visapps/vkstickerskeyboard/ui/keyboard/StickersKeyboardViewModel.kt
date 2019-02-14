package org.visapps.vkstickerskeyboard.ui.keyboard

import androidx.lifecycle.MutableLiveData
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

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    val loginStatus = MutableLiveData<Boolean>()
    val loadingState = MutableLiveData<Boolean>()
    val chats = MutableLiveData<List<Chat>>()
    val error = SingleLiveEvent<String>()

    private var job = Job()

    init{
        loadState()
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

    private fun getState() {

    }
}