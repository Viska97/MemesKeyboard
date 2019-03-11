package org.visapps.vkmemeskeyboard.util

import android.content.Context
import org.visapps.vkmemeskeyboard.data.backend.BackendRepository
import org.visapps.vkmemeskeyboard.data.vk.VKRepository
import org.visapps.vkmemeskeyboard.ui.keyboard.MemesKeyboardViewModel
import org.visapps.vkmemeskeyboard.ui.viewmodels.AllPacksViewModelFactory
import org.visapps.vkmemeskeyboard.ui.viewmodels.PackViewModelFactory
import org.visapps.vkmemeskeyboard.ui.viewmodels.SavedPacksViewModelFactory

object InjectorUtils {

    fun getVKRepository(context: Context) : VKRepository {
        return VKRepository.get(context)
    }

    fun getBackendRepository(context: Context) : BackendRepository {
        return BackendRepository.get(context)
    }

    fun getStickersKeyboardViewModel(context: Context) : MemesKeyboardViewModel {
        val backendRepository = getBackendRepository(context)
        val vkRepository = getVKRepository(context)
        return MemesKeyboardViewModel(backendRepository, vkRepository)
    }

    fun provideAllStickersViewModelFactory(context: Context): AllPacksViewModelFactory {
        val repository = getBackendRepository(context)
        return AllPacksViewModelFactory(repository)
    }

    fun providePackViewModelFactory(context: Context, packId : Int) : PackViewModelFactory {
        val repository = getBackendRepository(context)
        return PackViewModelFactory(repository, packId)
    }

    fun provideSavedStickersViewModelFactory(context: Context) : SavedPacksViewModelFactory {
        val repository = getBackendRepository(context)
        return SavedPacksViewModelFactory(repository)
    }

}