package org.visapps.vkstickerskeyboard.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.chat_item.view.*
import org.visapps.vkstickerskeyboard.GlideApp
import org.visapps.vkstickerskeyboard.R
import org.visapps.vkstickerskeyboard.data.models.Chat

class ChatAdapter(val context : Context) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private val chats = ArrayList<Chat>()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_item, p0, false))

    override fun getItemCount() = chats.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        GlideApp.with(context).load(chats[position].photo).circleCrop().into(holder.avatar)
    }

    fun updateChats(chats : List<Chat>){
        this.chats.clear()
        this.chats.addAll(chats)
    }

    fun clear(){
        chats.clear()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar = itemView.avatar
    }
}