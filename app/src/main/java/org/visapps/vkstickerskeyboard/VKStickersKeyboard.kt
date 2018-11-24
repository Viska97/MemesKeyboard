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
        const val TOKEN = "TOKEN"
        lateinit var instance: VKStickersKeyboard
            private set
    }

    private lateinit var tokenStatus : MutableLiveData<String>

    private val vkAccessTokenTracker = object : VKAccessTokenTracker(){
        override fun onVKAccessTokenChanged(oldToken: VKAccessToken?, newToken: VKAccessToken?) {
            if(newToken == null){
                defaultSharedPreferences.edit {
                    putString(TOKEN, null)
                }
                tokenStatus.postValue(null)
            }
            else{
                defaultSharedPreferences.edit {
                    putString(TOKEN, newToken.accessToken)
                }
                tokenStatus.postValue(newToken.accessToken)
            }
        }
    }

    override fun onCreate() {
        tokenStatus = MutableLiveData()
        tokenStatus.postValue(defaultSharedPreferences.getString(TOKEN,null))
        vkAccessTokenTracker.startTracking()
        VKSdk.initialize(applicationContext)
        super.onCreate()
        instance = this
        Log.e("vasily",VKSdk.getAccessToken().accessToken)
    }

    fun getTokenStatus() : LiveData<String>{
        return tokenStatus
    }
}