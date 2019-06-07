package org.visapps.vkmemeskeyboard.ui.viewmodels

import androidx.lifecycle.ViewModel;
import org.visapps.vkmemeskeyboard.data.backend.BackendRepository

class SavedPacksViewModel(val repository: BackendRepository) : ViewModel() {

    val packs = repository.getSavedPacks()
}
