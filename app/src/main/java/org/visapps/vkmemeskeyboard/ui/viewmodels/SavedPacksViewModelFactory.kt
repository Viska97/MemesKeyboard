package org.visapps.vkmemeskeyboard.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.visapps.vkmemeskeyboard.data.backend.BackendRepository

class SavedPacksViewModelFactory(private val backendRepository: BackendRepository)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SavedPacksViewModel(backendRepository) as T
    }
}