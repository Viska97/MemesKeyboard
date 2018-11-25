package org.visapps.vkstickerskeyboard.keyboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class KeyboardViewModel(application: Application) : AndroidViewModel(application) {

    private val LoginStatus = MutableLiveData<Boolean>()

    init{

    }

    override fun onCleared() {
        super.onCleared()
    }

    fun getLoginStatus() : LiveData<Boolean>
    {
        return LoginStatus
    }
}