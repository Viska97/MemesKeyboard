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

    private val vkAccessTokenTracker = object : VKAccessTokenTracker(){
        override fun onVKAccessTokenChanged(oldToken: VKAccessToken?, newToken: VKAccessToken?) {
            Log.e("vasily", "changed")
            if(newToken == null){

            }
            else{

            }
        }
    }

    companion object {
        const val TOKEN = "TOKEN"
        lateinit var instance: VKStickersKeyboard
            private set
    }

    override fun onCreate() {
        vkAccessTokenTracker.startTracking()
        VKSdk.initialize(this)
        super.onCreate()
        instance = this
    }
}