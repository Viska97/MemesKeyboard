package org.visapps.vkstickerskeyboard.ui.keyboard

import android.inputmethodservice.InputMethodService
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.keyboard_dialogs.view.*
import kotlinx.android.synthetic.main.keyboard_login.view.*
import kotlinx.android.synthetic.main.keyboard_main.view.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.visapps.vkstickerskeyboard.R
import org.visapps.vkstickerskeyboard.data.backend.BackendRepository
import org.visapps.vkstickerskeyboard.data.models.Dialog
import org.visapps.vkstickerskeyboard.data.vk.VKRepository
import org.visapps.vkstickerskeyboard.ui.activity.AuthActivity
import org.visapps.vkstickerskeyboard.ui.adapter.ChatAdapter
import org.visapps.vkstickerskeyboard.util.InjectorUtils
import org.visapps.vkstickerskeyboard.util.KeyboardState
import org.visapps.vkstickerskeyboard.util.switchVisibility

class StickersKeyboardService : LifecycleKeyboardService() {

    private lateinit var view : View
    private lateinit var viewModel : StickersKeyboardViewModel

    private lateinit var adapter : ChatAdapter

    override fun onCreate() {
        setTheme(R.style.AppTheme)
        super.onCreate()
    }

    override fun onCreateInputView(): View {
        view = layoutInflater.inflate(R.layout.keyboard_main,null)
        view.loginbutton.setOnClickListener {
            startActivity(intentFor<AuthActivity>().newTask())
        }
        view.chats.layoutManager = GridLayoutManager(this,3,GridLayoutManager.HORIZONTAL,false)
        view.chats.setHasFixedSize(true)
        adapter = ChatAdapter(this)
        view.chats.adapter = adapter
        viewModel = InjectorUtils.getStickersKeyboardViewModel(this)
        registerObservers()
        return view
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        //viewModel.loadState()
        super.onStartInputView(info, restarting)
    }

    override fun onFinishInput() {
        super.onFinishInput()
    }

    override fun onDestroy() {

    }

    private fun registerObservers() {
        viewModel.keyboardState.observe(this, Observer<Int> {
            view.login.visibility = switchVisibility(it == KeyboardState.NOTAUTHENTICATED)
            view.dialogs.visibility = switchVisibility(it == KeyboardState.DIALOGS)
        })
        viewModel.networkState.observe(this, Observer<Int> {

        })
        viewModel.dialogs.observe(this, Observer<PagedList<Dialog>> {

        })

    }
}