package org.visapps.vkmemeskeyboard.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.visapps.vkmemeskeyboard.GlideRequests
import org.visapps.vkmemeskeyboard.data.models.Meme

class MemesAdapter(private val glide: GlideRequests) : RecyclerView.Adapter<StickerItemViewHolder>() {

    private val stickers = ArrayList<Meme>()

    override fun getItemCount() = stickers.size

    override fun onBindViewHolder(holder: StickerItemViewHolder, position: Int) {
        holder.bind(stickers[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerItemViewHolder {
        return StickerItemViewHolder.create(parent, glide)
    }

    fun updateStickers(memes : List<Meme>) {
        this.stickers.clear()
        this.stickers.addAll(memes)
    }

}