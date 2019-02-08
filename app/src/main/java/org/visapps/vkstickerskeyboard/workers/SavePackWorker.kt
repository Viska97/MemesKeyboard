package org.visapps.vkstickerskeyboard.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.visapps.vkstickerskeyboard.data.backend.BackendRepository
import org.visapps.vkstickerskeyboard.data.models.PackStatus
import org.visapps.vkstickerskeyboard.util.InjectorUtils

class SavePackWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val packId = inputData.getInt("packId", 0)
        val repository = InjectorUtils.getBackendRepository(applicationContext)
        val status = repository.getPackStatus(packId)
        when (status) {
            PackStatus.NOTSAVED -> return savePack(packId, repository)
            PackStatus.SAVED -> return removePack(packId, repository)
        }
        return Result.failure()
    }

    private fun savePack(packId: Int, repository: BackendRepository): Result {
        return Result.success()
    }

    private fun removePack(packId: Int, repository: BackendRepository): Result {
        return Result.success()
    }
}