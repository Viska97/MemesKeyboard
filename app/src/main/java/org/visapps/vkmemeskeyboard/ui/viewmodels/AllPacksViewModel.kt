package org.visapps.vkmemeskeyboard.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel;
import org.visapps.vkmemeskeyboard.data.backend.BackendRepository

class AllPacksViewModel(private val repository: BackendRepository) : ViewModel() {
    private val search = MutableLiveData<String>()
    private val status = map(search) {
        repository.searchPacks(it, 20)
    }
    val posts = switchMap(status, { it.pagedList })!!
    val networkState = switchMap(status, { it.networkState })!!
    val refreshState = switchMap(status, { it.refreshState })!!

    fun refresh() {
        status.value?.refresh?.invoke()
    }

    fun showPacks(searchText: String): Boolean {
        if (search.value == searchText) {
            return false
        }
        search.value = searchText
        return true
    }

    fun reload() {
        val listing = status?.value
        listing?.retry?.invoke()
    }
}
