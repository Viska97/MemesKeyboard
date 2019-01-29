package org.visapps.vkstickerskeyboard.ui.keyboard

import android.util.Log
import androidx.core.content.edit
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKAccessTokenTracker
import com.vk.sdk.VKSdk
import kotlinx.coroutines.*
import org.jetbrains.anko.defaultSharedPreferences
import org.visapps.vkstickerskeyboard.VKStickersKeyboard
import org.visapps.vkstickerskeyboard.data.response.Result
import org.visapps.vkstickerskeyboard.data.vk.VKRepository
import kotlin.coroutines.CoroutineContext

class StickersPresenter : StickersContract.Presenter, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() =  job + Dispatchers.Main

    private var view : StickersContract.View? = null
    private var job = Job()

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

    override fun onStart() {
        view?.updateLoginStatus(VKSdk.isLoggedIn())
        if(VKSdk.isLoggedIn()){
            loadChats()
        }
    }

    override fun onStop() {
        job.cancel()
    }

    override fun detachView() {
        view = null
    }

    override fun attach(view: StickersContract.View) {
        this.view = view
        //vkAccessTokenTracker.startTracking()
    }

    override fun loadChats() {
        this.launch(context = coroutineContext) {
            view?.clearChats()
            view?.updateLoadingState(true)
            val result = VKRepository.get().getChats()
            view?.updateLoadingState(false)
            when(result){
                is Result.Success -> view?.updateChats(result.data!!)
                is Result.Error -> view?.showError(result.exception.message!!)
            }
        }
    }
}