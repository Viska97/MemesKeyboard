package org.visapps.vkstickerskeyboard

import android.app.Application
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKAccessTokenTracker
import com.vk.sdk.VKSdk
import org.jetbrains.anko.defaultSharedPreferences

class VKStickersKeyboard : Application() {

    companion object {
        const val TOKEN = "TOKEN"
        lateinit var instance: VKStickersKeyboard
            private set
    }

    private lateinit var loginStatus : MutableLiveData<Boolean>

    private val vkAccessTokenTracker = object : VKAccessTokenTracker(){
        override fun onVKAccessTokenChanged(oldToken: VKAccessToken?, newToken: VKAccessToken?) {
            Log.e("vasily","changed")
            if(newToken == null){
                defaultSharedPreferences.edit {
                    putString(TOKEN, null)
                }
                loginStatus.postValue(false)
            }
            else{
                defaultSharedPreferences.edit {
                    putString(TOKEN, newToken.accessToken)
                }
            }
        }
    }

    override fun onCreate() {
        vkAccessTokenTracker.startTracking()
        VKSdk.initialize(applicationContext)
        super.onCreate()
        instance = this
    }

    fun getLoginStatus() : LiveData<Boolean>{
        if(!this::loginStatus.isInitialized){
            loginStatus = MutableLiveData()
            loginStatus.postValue(VKSdk.isLoggedIn())
        }
        return loginStatus
    }
}