package org.visapps.vkstickerskeyboard

import android.app.Application
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKAccessTokenTracker
import com.vk.sdk.VKSdk
import org.jetbrains.anko.defaultSharedPreferences

class VKStickersKeyboard : Application() {

    companion object {
        lateinit var instance: VKStickersKeyboard
            private set
    }

    override fun onCreate() {
        VKSdk.initialize(this)
        instance = this
        super.onCreate()
    }
}