package org.visapps.vkstickerskeyboard

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.executor.GlideExecutor
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import java.io.InputStream
import java.util.concurrent.TimeUnit

@GlideModule
class VKStickersGlide : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setSourceExecutor(GlideExecutor.newSourceExecutor(100,"source-thread",GlideExecutor.UncaughtThrowableStrategy.DEFAULT))
        builder.setDiskCacheExecutor(GlideExecutor.newDiskCacheExecutor(100, "diskcache-thread", GlideExecutor.UncaughtThrowableStrategy.DEFAULT))
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val builder = OkHttpClient.Builder()
        builder.connectionPool(ConnectionPool(100, 3, TimeUnit.SECONDS))
        val factory = OkHttpUrlLoader.Factory(builder.build())
        registry.replace(GlideUrl::class.java, InputStream::class.java, factory)
    }
}