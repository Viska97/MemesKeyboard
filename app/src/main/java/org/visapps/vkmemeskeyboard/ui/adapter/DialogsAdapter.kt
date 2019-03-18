package org.visapps.vkmemeskeyboard.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.dialog_item.view.*
import org.visapps.vkmemeskeyboard.GlideApp
import org.visapps.vkmemeskeyboard.R
import org.visapps.vkmemeskeyboard.data.models.Dialog

class DialogsAdapter(val context : Context) : RecyclerView.Adapter<DialogsAdapter.ViewHolder>() {

    private val dialogs = ArrayList<Dialog>()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.dialog_item,
                p0,
                false
            )
        )

    override fun getItemCount() = dialogs.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        GlideApp.with(context).load(dialogs[position].photo).circleCrop().into(holder.avatar)
    }

    fun updateDialogs(dialogs : List<Dialog>){
        this.dialogs.clear()
        this.dialogs.addAll(dialogs)
    }

    fun clear(){
        dialogs.clear()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar = itemView.avatar
    }
}