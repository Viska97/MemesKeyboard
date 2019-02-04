package org.visapps.vkstickerskeyboard.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.visapps.vkstickerskeyboard.GlideRequests
import org.visapps.vkstickerskeyboard.data.models.Sticker

class StickersAdapter(private val glide: GlideRequests) : RecyclerView.Adapter<StickerItemViewHolder>() {

    private val stickers = ArrayList<Sticker>()

    override fun getItemCount() = stickers.size

    override fun onBindViewHolder(holder: StickerItemViewHolder, position: Int) {
        holder.bind(stickers[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerItemViewHolder {
        return StickerItemViewHolder.create(parent, glide)
    }

    fun updateStickers(stickers : List<Sticker>) {
        this.stickers.clear()
        this.stickers.addAll(stickers)
    }

}