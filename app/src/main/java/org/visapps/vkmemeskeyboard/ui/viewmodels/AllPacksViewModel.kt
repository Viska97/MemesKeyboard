package org.visapps.vkmemeskeyboard.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import kotlinx.coroutines.*
import org.visapps.vkmemeskeyboard.data.backend.BackendRepository
import org.visapps.vkmemeskeyboard.data.models.Pack
import org.visapps.vkmemeskeyboard.util.NetworkState
import org.visapps.vkmemeskeyboard.util.Result
import kotlin.coroutines.CoroutineContext

class AllPacksViewModel(private val repository: BackendRepository) : ViewModel(), CoroutineScope {

    companion object {
        private const val pageSize = 20
        private const val prefetchDistance = 5;
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private val job = Job()
    private var isRequestInProgress = false

    private val boundaryCallback = object : PagedList.BoundaryCallback<Pack>() {
        override fun onZeroItemsLoaded() {
            loadPacks()
        }

        override fun onItemAtEndLoaded(itemAtEnd: Pack) {
            loadPacks()
        }
    }

    private val search = MutableLiveData<String>()
    val packs: LiveData<PagedList<Pack>> = switchMap(search) {
        repository.searchPacks(it, pageSize, prefetchDistance, boundaryCallback)
    }
    val networkState = MutableLiveData<Int>()
    val refreshState = MutableLiveData<Int>()

    init {
        refreshState.postValue(null)
        networkState.postValue(null)
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun showPacks(searchText: String): Boolean {
        if (search.value == searchText) {
            return false
        }
        search.value = searchText
        return true
    }

    fun refreshPacks() {
        launch(coroutineContext) {
            refreshState.postValue(NetworkState.RUNNING)
            val searchText = search.value ?: ""
            val result = withContext(Dispatchers.IO){ repository.refreshPacks(searchText, pageSize) }
            when (result) {
                is Result.Success -> {
                    refreshState.postValue(NetworkState.SUCCESS)
                }
                is Result.Error -> {
                    refreshState.postValue(NetworkState.FAILED)
                }
            }
        }
    }

    fun loadPacks() {
        if (isRequestInProgress) return
        isRequestInProgress = true
        launch(coroutineContext) {
            networkState.postValue(NetworkState.RUNNING)
            val searchText = search.value ?: ""
            val result = withContext(Dispatchers.IO){ repository.getPacks(searchText, pageSize) }
            when(result){
                is Result.Success -> {
                    networkState.postValue(NetworkState.SUCCESS)
                }
                is Result.Error -> {
                    networkState.postValue(NetworkState.FAILED)
                }
            }
            isRequestInProgress = false
        }
    }

}
