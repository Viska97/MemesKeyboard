package org.visapps.vkstickerskeyboard.ui

import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.core.content.edit
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError
import org.visapps.vkstickerskeyboard.R

import kotlinx.android.synthetic.main.activity_auth.*
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.toolbar
import org.visapps.vkstickerskeyboard.VKStickersKeyboard.Companion.TOKEN

class AuthActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        retrybutton.setOnClickListener {
            VKSdk.login(this, "messages")
        }
        if(!VKSdk.isLoggedIn()){
            VKSdk.login(this, "messages")
        }
        else{
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {
                override fun onResult(res: VKAccessToken) {
                    noauth.visibility = View.GONE
                    defaultSharedPreferences.edit {
                        putString(TOKEN,VKAccessToken.ACCESS_TOKEN)
                    }
                    finish()
                }
                override fun onError(error: VKError) {
                    noauth.visibility = View.VISIBLE
                }
            })
        ) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }



}
