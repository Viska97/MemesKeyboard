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
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.keyboard_main.view.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.visapps.vkstickerskeyboard.R
import org.visapps.vkstickerskeyboard.data.models.Chat
import org.visapps.vkstickerskeyboard.data.vk.ConversationsResponse
import org.visapps.vkstickerskeyboard.ui.AuthActivity
import org.visapps.vkstickerskeyboard.ui.adapter.ChatAdapter

class StickersKeyboardService : InputMethodService(), LifecycleOwner {


    val registry = LifecycleRegistry(this)
    private lateinit var view : View
    private lateinit var viewModel : StickersKeyboardViewModel

    private lateinit var adapter : ChatAdapter


    override fun getLifecycle(): Lifecycle {
        return registry
    }

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
        viewModel = StickersKeyboardViewModel()
        registerObservers()
        registry.markState(Lifecycle.State.CREATED)
        return view
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        registry.markState(Lifecycle.State.RESUMED)
        viewModel.loadState()
        Log.i("Vasily", "Start")
        super.onStartInputView(info, restarting)
    }

    override fun onFinishInput() {
        Log.i("Vasily", "Stop")
        super.onFinishInput()
    }

    override fun onDestroy() {
        onFinishInput()
        registry.markState(Lifecycle.State.DESTROYED)
        Log.i("Vasily", "Destroy")
    }



    private fun registerObservers() {
        viewModel.loginStatus.observe(this, Observer { isLoggedIn ->
            isLoggedIn?.let {
                if (isLoggedIn) {
                    view.loginbutton.visibility = View.GONE
                    view.mainview.visibility = View.VISIBLE
                } else {
                    view.loginbutton.visibility = View.VISIBLE
                    view.mainview.visibility = View.GONE
                }
            }
        })
        viewModel.chats.observe(this, Observer { chats ->
            if(chats == null){
                adapter.clear()
            }
            else{
                adapter.updateChats(chats)
            }
            adapter.notifyDataSetChanged()
        })
        viewModel.loadingState.observe(this, Observer { loading ->
            loading?.let {
                if (loading) {
                    view.progress.visibility = View.VISIBLE
                } else {
                    view.progress.visibility = View.GONE
                }
            }
        })
        viewModel.error.observe(this, Observer { error ->
            error?.let {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        })
    }
}