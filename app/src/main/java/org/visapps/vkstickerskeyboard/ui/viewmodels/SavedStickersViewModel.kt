package org.visapps.vkstickerskeyboard.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel;
import org.visapps.vkstickerskeyboard.data.backend.BackendRepository
import org.visapps.vkstickerskeyboard.data.models.Pack

class SavedStickersViewModel(val repository: BackendRepository) : ViewModel() {

    val packs = repository.getSavedPacks()
}
