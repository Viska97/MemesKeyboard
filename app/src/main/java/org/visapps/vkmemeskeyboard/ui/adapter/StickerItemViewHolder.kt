package org.visapps.vkmemeskeyboard.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.visapps.vkmemeskeyboard.GlideRequests
import org.visapps.vkmemeskeyboard.R
import org.visapps.vkmemeskeyboard.data.models.Meme
import org.visapps.vkmemeskeyboard.util.MemeUrl

class StickerItemViewHolder(private val view: View, private val glide: GlideRequests) :
    RecyclerView.ViewHolder(view)  {

    private val image = view.findViewById<ImageView>(R.id.image)
    private var meme : Meme? = null

    fun bind(meme : Meme?) {
        this.meme = meme
        meme?.let {
            glide.load(MemeUrl(it.image, it.pack_id, it.id)).diskCacheStrategy(DiskCacheStrategy.ALL).onlyRetrieveFromCache(true).fitCenter().into(image)
        }
    }

    companion object {
        fun create(parent: ViewGroup, glide: GlideRequests): StickerItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.meme_item, parent, false)
            return StickerItemViewHolder(view, glide)
        }
    }

}