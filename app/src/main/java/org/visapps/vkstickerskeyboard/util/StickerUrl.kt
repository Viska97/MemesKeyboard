package org.visapps.vkstickerskeyboard.util

import com.bumptech.glide.load.model.GlideUrl

class StickerUrl(private var url : String, private var packId : Int, private var stickerId : Int) : GlideUrl(url) {

    override fun getCacheKey(): String {
        return packId.toString() + "_" + stickerId.toString()
    }
}