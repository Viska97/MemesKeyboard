package org.visapps.vkmemeskeyboard.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.visapps.vkmemeskeyboard.data.backend.BackendRepository

class PackViewModelFactory(private val backendRepository: BackendRepository, private val packId: Int) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PackViewModel(backendRepository, packId) as T
    }
}