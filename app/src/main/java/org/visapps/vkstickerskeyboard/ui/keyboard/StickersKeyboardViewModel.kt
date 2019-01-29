package org.visapps.vkstickerskeyboard.ui.keyboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.visapps.vkstickerskeyboard.SingleLiveEvent
import org.visapps.vkstickerskeyboard.data.models.Chat

class StickersKeyboardViewModel : ViewModel() {

    val loginStatus = MutableLiveData<Boolean>()
    val loadingState = MutableLiveData<Boolean>()
    val chats = MutableLiveData<List<Chat>>()
    val error = SingleLiveEvent<String>()

    init{

    }


}