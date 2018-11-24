package org.visapps.vkstickerskeyboard.keyboard

import android.inputmethodservice.InputMethodService
import android.view.View
import androidx.lifecycle.*
import kotlinx.android.synthetic.main.keyboard_main.view.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.visapps.vkstickerskeyboard.R
import org.visapps.vkstickerskeyboard.VKStickersKeyboard
import org.visapps.vkstickerskeyboard.ui.AuthActivity

class StickersKeyboardService : InputMethodService(), LifecycleOwner {

    private val lifecycle = LifecycleRegistry(this);

    override fun onCreate() {
        setTheme(R.style.AppTheme)
        super.onCreate()
    }

    companion object {
        fun UpdateStatus() {

        }
    }

    override fun onCreateInputView(): View {
        val view = layoutInflater.inflate(R.layout.keyboard_main,null)
        view.loginbutton.setOnClickListener {
            startActivity(intentFor<AuthActivity>().newTask())
        }
        VKStickersKeyboard.instance.getTokenStatus().observe(this, Observer { it->
            if(it == null){
                view.loginbutton.visibility = View.VISIBLE
            }
            else{
                view.loginbutton.visibility = View.GONE
            }
        })
        return view
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }
}