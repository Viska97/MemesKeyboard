package org.visapps.vkmemeskeyboard.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.dialog_item.view.*
import org.visapps.vkmemeskeyboard.GlideRequests
import org.visapps.vkmemeskeyboard.R
import org.visapps.vkmemeskeyboard.data.models.Dialog

class DialogViewHolder(private val view: View,
                       private val glide: GlideRequests,
                       private val dialogCallback: (Int) -> Unit) : RecyclerView.ViewHolder(view) {

    private val avatar = view.findViewById<ImageView>(R.id.avatar)
    private var dialog : Dialog? = null

    init {
        avatar.setOnClickListener {
            dialog?.let {
                dialogCallback.invoke(it.peerId)
            }
        }
    }

    fun bind(dialog: Dialog?) {
        this.dialog = dialog
        dialog?.let {
            glide.load(it.photo)
                .circleCrop()
                .into(avatar)
        }
    }

    companion object {
        fun create(parent: ViewGroup, glide: GlideRequests, dialogCallback: (Int) -> Unit): DialogViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.dialog_item, parent, false)
            return DialogViewHolder(view, glide, dialogCallback)
        }
    }

}