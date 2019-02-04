package org.visapps.vkstickerskeyboard.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import org.visapps.vkstickerskeyboard.GlideRequests
import org.visapps.vkstickerskeyboard.R
import org.visapps.vkstickerskeyboard.data.models.Sticker

class StickerItemViewHolder(private val view: View, private val glide: GlideRequests) :
    RecyclerView.ViewHolder(view)  {

    private val image = view.findViewById<ImageView>(R.id.image)
    private var sticker : Sticker? = null

    fun bind(sticker : Sticker?) {
        this.sticker = sticker
        sticker?.let {
            glide.load(it.image).centerCrop().into(image)
        }
    }

    companion object {
        fun create(parent: ViewGroup, glide: GlideRequests): StickerItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.sticker_item, parent, false)
            return StickerItemViewHolder(view, glide)
        }
    }

}