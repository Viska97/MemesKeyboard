package org.visapps.vkmemeskeyboard

import android.app.Application
import com.vk.sdk.VKSdk

class VKMemesKeyboard : Application() {

    companion object {
        lateinit var instance: VKMemesKeyboard
            private set
    }

    override fun onCreate() {
        VKSdk.initialize(this)
        instance = this
        super.onCreate()
    }

}