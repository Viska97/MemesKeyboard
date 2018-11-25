package org.visapps.vkstickerskeyboard.keyboard

import android.inputmethodservice.InputMethodService
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.*
import com.vk.sdk.VKSdk
import kotlinx.android.synthetic.main.keyboard_main.view.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.visapps.vkstickerskeyboard.R
import org.visapps.vkstickerskeyboard.VKStickersKeyboard
import org.visapps.vkstickerskeyboard.ui.AuthActivity

class StickersKeyboardService : InputMethodService() {

    private lateinit var view : View
    private lateinit var viewModel : KeyboardViewModel

    override fun onCreate() {
        setTheme(R.style.AppTheme)
        super.onCreate()
    }

    override fun onCreateInputView(): View {
        view = layoutInflater.inflate(R.layout.keyboard_main,null)
        view.loginbutton.setOnClickListener {
            startActivity(intentFor<AuthActivity>().newTask())
        }
        return view
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        viewModel = ViewModelProviders.of(this).
        super.onStartInputView(info, restarting)
    }
}