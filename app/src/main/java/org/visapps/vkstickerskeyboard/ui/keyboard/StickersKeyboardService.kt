package org.visapps.vkstickerskeyboard.ui.keyboard

import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.keyboard_main.view.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.visapps.vkstickerskeyboard.R
import org.visapps.vkstickerskeyboard.ui.AuthActivity

class StickersKeyboardService : InputMethodService(), StickersContract.View {

    private lateinit var view : View
    private lateinit var presenter: StickersPresenter

    override fun onCreate() {
        setTheme(R.style.AppTheme)
        super.onCreate()
    }

    override fun onCreateInputView(): View {
        view = layoutInflater.inflate(R.layout.keyboard_main,null)
        view.loginbutton.setOnClickListener {
            startActivity(intentFor<AuthActivity>().newTask())
        }
        presenter = StickersPresenter()
        presenter.attach(this)
        return view
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
    }

    override fun onFinishInput() {
        super.onFinishInput()
    }

    override fun updateLoginStatus(loggedIn: Boolean) {
        if(loggedIn){
            view.loginbutton.visibility = View.GONE
        }
        else{
            view.loginbutton.visibility = View.VISIBLE
        }
    }
}