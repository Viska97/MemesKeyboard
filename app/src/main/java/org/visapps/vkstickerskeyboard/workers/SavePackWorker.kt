package org.visapps.vkstickerskeyboard.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.*
import org.visapps.vkstickerskeyboard.GlideApp
import org.visapps.vkstickerskeyboard.GlideRequests
import org.visapps.vkstickerskeyboard.data.backend.BackendRepository
import org.visapps.vkstickerskeyboard.data.models.PackStatus
import org.visapps.vkstickerskeyboard.util.InjectorUtils
import org.visapps.vkstickerskeyboard.util.NetworkState
import org.visapps.vkstickerskeyboard.util.StickerUrl
import org.visapps.vkstickerskeyboard.util.Result as StickersResult

class SavePackWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val packId = inputData.getInt("packId", 0)
        val repository = InjectorUtils.getBackendRepository(applicationContext)
        val glide = GlideApp.with(applicationContext)
        val status = repository.getPackStatus(packId)
        when (status) {
            PackStatus.NOTSAVED -> return savePack(packId, repository, glide)
            PackStatus.SAVED -> return removePack(packId, repository)
        }
        return Result.failure()
    }

    private fun savePack(packId: Int, repository: BackendRepository, glide : GlideRequests): Result = runBlocking {
        Log.i("Vasily", "Saving pack started")
        repository.updatePackStatus(packId, PackStatus.INPROGRESS)
        Log.i("Vasily", "Status updated")
        val stickers = repository.getStickers(packId, forceNetwork = true)
        if(stickers is StickersResult.Success){
            Log.i("Vasily", "Started glide")
            stickers.data.map {sticker ->
                launch(Dispatchers.IO) {
                    glide.load(StickerUrl(sticker.image, sticker.pack_id, sticker.id)).priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.DATA).submit().get()
                    Log.i("Vasily", "Download complete ${sticker.id}")
                }
            }.forEach { it.join() }
            repository.savePack(packId, stickers.data)
            Log.i("Vasily", "Pack saved")
            return@runBlocking Result.success()
        }
        return@runBlocking Result.success()
    }

    private fun removePack(packId: Int, repository: BackendRepository): Result = runBlocking {
        repository.updatePackStatus(packId, PackStatus.INPROGRESS)
        repository.removePack(packId)
        return@runBlocking Result.success()
    }
}