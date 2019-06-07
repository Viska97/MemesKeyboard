package org.visapps.vkmemeskeyboard.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError
import kotlinx.android.synthetic.main.activity_auth.*
import org.visapps.vkmemeskeyboard.R
import org.visapps.vkmemeskeyboard.util.InjectorUtil

class AuthActivity : Activity() {

    companion object {
        private const val scope = "messages"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        retrybutton.setOnClickListener {
            VKSdk.login(this, scope)
        }
        if(!VKSdk.isLoggedIn()){
            VKSdk.login(this, scope)
        }
        else{
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        InjectorUtil.getVKRepository(this).handleLogIn()
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {
                override fun onResult(res: VKAccessToken) {
                    noauth.visibility = View.GONE
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
