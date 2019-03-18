package org.visapps.vkmemeskeyboard.ui.keyboard

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.keyboard_dialogs.view.*
import kotlinx.android.synthetic.main.keyboard_login.view.*
import kotlinx.android.synthetic.main.keyboard_main.view.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.visapps.vkmemeskeyboard.R
import org.visapps.vkmemeskeyboard.data.models.Dialog
import org.visapps.vkmemeskeyboard.ui.activity.AuthActivity
import org.visapps.vkmemeskeyboard.ui.adapter.DialogsAdapter
import org.visapps.vkmemeskeyboard.util.InjectorUtil
import org.visapps.vkmemeskeyboard.util.NetworkState
import org.visapps.vkmemeskeyboard.util.switchVisibility
import org.visapps.vkmemeskeyboard.util.toVisibility

class MemesKeyboardService : LifecycleKeyboardService() {

    companion object {
        const val TAG = "MKeyboard"
    }

    private lateinit var view : View
    private lateinit var viewModel : MemesKeyboardViewModel

    private lateinit var adapter : DialogsAdapter

    override fun onCreate() {
        setTheme(R.style.AppTheme)
        super.onCreate()
    }

    override fun onCreateInputView(): View {
        view = layoutInflater.inflate(R.layout.keyboard_main,null)
        view.loginbutton.setOnClickListener {
            startActivity(intentFor<AuthActivity>().newTask())
        }
        view.list_dialogs.layoutManager = GridLayoutManager(this,3,GridLayoutManager.HORIZONTAL,false)
        view.list_dialogs.setHasFixedSize(true)
        adapter = DialogsAdapter(this)
        view.list_dialogs.adapter = adapter
        viewModel = InjectorUtil.getStickersKeyboardViewModel(this)
        registerObservers()
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onCleared()
    }

    private fun registerObservers() {
        viewModel.keyboardState.observe(this, Observer<KeyboardState> {
            view.login.visibility =
                switchVisibility(it == KeyboardState.NOT_AUTHENTICATED)
            view.dialogs.visibility =
                switchVisibility(it == KeyboardState.DIALOGS)
        })
        viewModel.networkState.observe(this, Observer<NetworkState> {
            view.update_button.visibility =
                toVisibility(it == NetworkState.SUCCESS)
        })
        viewModel.refreshState.observe(this, Observer<NetworkState> {
            view.progress.visibility =
                toVisibility(it == NetworkState.RUNNING)
            view.list_dialogs.visibility =
                toVisibility(it == NetworkState.SUCCESS)
            view.alert.visibility = toVisibility(it == NetworkState.FAILED)
        })
        viewModel.dialogs.observe(this, Observer {
            it?.let {
                Log.i(TAG, "dialogs size ${it.size}")
                adapter.updateDialogs(it)
                adapter.notifyDataSetChanged()
            }
        })

    }
}