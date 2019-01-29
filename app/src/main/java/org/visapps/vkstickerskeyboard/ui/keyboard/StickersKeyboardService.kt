package org.visapps.vkstickerskeyboard.ui.keyboard

import android.inputmethodservice.InputMethodService
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
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

class StickersKeyboardService : LifecycleKeyboardService() {

    private lateinit var view : View
    private val viewModel = StickersKeyboardViewModel()

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
        registerObservers()
        return view
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        Log.i("Vasily", "Start")
        super.onStartInputView(info, restarting)
    }

    override fun onFinishInput() {
        Log.i("Vasily", "Stop")
        super.onFinishInput()
    }

    fun registerObservers() {
        viewModel.loginStatus.observe(this, Observer {isLoggedIn->
            isLoggedIn?.let {
                if(isLoggedIn){
                    view.loginbutton.visibility = View.GONE
                    view.mainview.visibility = View.VISIBLE
                }
                else{
                    view.loginbutton.visibility = View.VISIBLE
                    view.mainview.visibility = View.GONE
                }
            }
        })
        viewModel.chats.observe(this, Observer { chats->
            chats?.let {

            }
        })
    }



    override fun updateLoginStatus(loggedIn: Boolean) {
        if(loggedIn){
            view.loginbutton.visibility = View.GONE
            view.mainview.visibility = View.VISIBLE
        }
        else{
            view.loginbutton.visibility = View.VISIBLE
            view.mainview.visibility = View.GONE
        }
    }

    override fun updateChats(chats: ConversationsResponse) {
        val result: List<Chat> = chats.response!!.profiles!!.map { Chat(it.id, it.photo100) };
        adapter.updateChats(result)
        adapter.notifyDataSetChanged()
    }

    override fun clearChats() {
        adapter.clear()
        adapter.notifyDataSetChanged()
    }

    override fun updateLoadingState(isLoading: Boolean) {
        if(isLoading){view.progress.visibility = View.VISIBLE}else{view.progress.visibility = View.GONE}
    }

    override fun showError(message: String) {
        Toast.makeText(this, message,Toast.LENGTH_SHORT).show()
    }
}