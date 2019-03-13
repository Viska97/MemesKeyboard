package org.visapps.vkmemeskeyboard.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.visapps.vkmemeskeyboard.GlideApp
import org.visapps.vkmemeskeyboard.data.models.PackStatus
import org.visapps.vkmemeskeyboard.util.InjectorUtil
import org.visapps.vkmemeskeyboard.util.MemeUrl
import org.visapps.vkmemeskeyboard.util.Result as StickersResult

class SavePackWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override val coroutineContext = Dispatchers.IO

    override suspend fun doWork(): Result = coroutineScope {
        val packId = inputData.getInt("packId", 0)
        val repository = InjectorUtil.getBackendRepository(applicationContext)
        val glide = GlideApp.with(applicationContext)
        val status = repository.getPackStatus(packId)
        if(status == PackStatus.NOTSAVED){
            repository.updatePackStatus(packId, PackStatus.IN_PROGRESS)
            val stickers = repository.getMemes(packId, forceNetwork = true)
            if(stickers is StickersResult.Success){
                stickers.data.map {sticker ->
                    async {
                        glide.load(
                            MemeUrl(
                                sticker.image,
                                sticker.pack_id,
                                sticker.id
                            )
                        ).priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.DATA).submit().get()
                    }
                }.forEach { it.join() }
                repository.savePack(packId, stickers.data)
                return@coroutineScope Result.success()
            }
        }
        else{
            repository.updatePackStatus(packId, PackStatus.IN_PROGRESS)
            repository.removePack(packId)
            return@coroutineScope Result.success()
        }
        return@coroutineScope Result.failure()
    }

}