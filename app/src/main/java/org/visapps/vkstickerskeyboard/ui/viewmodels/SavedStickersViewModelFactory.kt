package org.visapps.vkstickerskeyboard.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.visapps.vkstickerskeyboard.data.backend.BackendRepository

class SavedStickersViewModelFactory(private val backendRepository: BackendRepository)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SavedStickersViewModel(backendRepository) as T
    }
}