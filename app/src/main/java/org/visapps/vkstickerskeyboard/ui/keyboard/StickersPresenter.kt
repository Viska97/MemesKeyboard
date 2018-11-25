package org.visapps.vkstickerskeyboard.ui.keyboard

import android.util.Log
import androidx.core.content.edit
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKAccessTokenTracker
import com.vk.sdk.VKSdk
import kotlinx.coroutines.*
import org.jetbrains.anko.defaultSharedPreferences
import org.visapps.vkstickerskeyboard.VKStickersKeyboard
import org.visapps.vkstickerskeyboard.data.vk.VKRepository

class StickersPresenter : StickersContract.Presenter {

    private var view : StickersContract.View? = null
    private var chatsJob: Job? = null

    private val vkAccessTokenTracker = object : VKAccessTokenTracker(){
        override fun onVKAccessTokenChanged(oldToken: VKAccessToken?, newToken: VKAccessToken?) {
            Log.e("vasily", "changed")
            if(newToken == null){
                view?.updateLoginStatus(false)
            }
            else{
                view?.updateLoginStatus(true)
            }
        }
    }

    override fun detachView() {
        view = null
    }

    override fun attach(view: StickersContract.View) {
        this.view = view
        //vkAccessTokenTracker.startTracking()
        view.updateLoginStatus(VKSdk.isLoggedIn())
    }

    override fun loadChats() {
        chatsJob = CoroutineScope(Dispatchers.IO).launch {
            val result = VKRepository.get().getChats()
        }
    }
}