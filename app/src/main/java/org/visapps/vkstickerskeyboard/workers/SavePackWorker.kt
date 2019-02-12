package org.visapps.vkstickerskeyboard.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import kotlinx.coroutines.*
import org.visapps.vkstickerskeyboard.data.backend.BackendRepository
import org.visapps.vkstickerskeyboard.data.models.PackStatus
import org.visapps.vkstickerskeyboard.util.InjectorUtils
import org.visapps.vkstickerskeyboard.util.Result as StickersResult

class SavePackWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val packId = inputData.getInt("packId", 0)
        val repository = InjectorUtils.getBackendRepository(applicationContext)
        val glide = Glide.with(applicationContext)
        val status = repository.getPackStatus(packId)
        when (status) {
            PackStatus.NOTSAVED -> return savePack(packId, repository, glide)
            PackStatus.SAVED -> return removePack(packId, repository)
        }
        return Result.failure()
    }

    private fun savePack(packId: Int, repository: BackendRepository, glide : RequestManager): Result = runBlocking {
        repository.updatePackStatus(packId, PackStatus.INPROGRESS)
        val stickers = repository.getStickers(packId, forceNetwork = true)
        if(stickers is StickersResult.Success){
            stickers.data.map {sticker ->
                launch {
                    glide.downloadOnly().load(sticker.image).submit().get()
                }
            }.forEach { it.join() }
            repository.savePack(packId, stickers.data)
            return@runBlocking Result.success()
        }
        repository.updatePackStatus(packId, PackStatus.NOTSAVED)
        return@runBlocking Result.retry()
    }

    private fun removePack(packId: Int, repository: BackendRepository): Result = runBlocking {
        repository.updatePackStatus(packId, PackStatus.INPROGRESS)
        repository.removePack(packId)
        return@runBlocking Result.success()
    }
}