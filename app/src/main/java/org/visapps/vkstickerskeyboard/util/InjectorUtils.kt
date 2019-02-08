package org.visapps.vkstickerskeyboard.util

import android.content.Context
import org.visapps.vkstickerskeyboard.data.backend.BackendRepository
import org.visapps.vkstickerskeyboard.ui.viewmodels.AllStickersViewModelFactory
import org.visapps.vkstickerskeyboard.ui.viewmodels.PackViewModelFactory
import org.visapps.vkstickerskeyboard.ui.viewmodels.SavedStickersViewModelFactory

object InjectorUtils {

    fun getBackendRepository(context: Context) : BackendRepository {
        return BackendRepository.get(context)
    }

    fun provideAllStickersViewModelFactory(context: Context): AllStickersViewModelFactory {
        val repository = getBackendRepository(context)
        return AllStickersViewModelFactory(repository)
    }

    fun providePackViewModelFactory(context: Context, packId : Int) : PackViewModelFactory {
        val repository = getBackendRepository(context)
        return PackViewModelFactory(repository, packId)
    }

    fun provideSavedStickersViewModelFactory(context: Context) : SavedStickersViewModelFactory {
        val repository = getBackendRepository(context)
        return SavedStickersViewModelFactory(repository)
    }

}