package org.visapps.vkmemeskeyboard.data.backend

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.visapps.vkmemeskeyboard.data.database.AppDatabase
import org.visapps.vkmemeskeyboard.data.models.Pack
import org.visapps.vkmemeskeyboard.util.NetworkState
import org.visapps.vkmemeskeyboard.util.Result

class PacksBoundaryCallback(
    private val searchText: String,
    private val pageSize: Int,
    private val dataSource: BackendDataSource,
    private val database : AppDatabase
) : PagedList.BoundaryCallback<Pack>(){

    var networkState = MutableLiveData<Int>()

    private var isRequestInProgress = false

    override fun onZeroItemsLoaded() {
        loadPacks()
    }

    override fun onItemAtEndLoaded(itemAtEnd: Pack) {
        loadPacks()
    }

    fun reload() {
        loadPacks()
    }

    private fun loadPacks() {
        if (isRequestInProgress) return
        isRequestInProgress = true
        GlobalScope.launch(Dispatchers.IO) {
            networkState.postValue(NetworkState.RUNNING)
            val offset = database.packDao().updatedPacksCount()
            val result = dataSource.searchPacks(searchText, pageSize, offset)
            when(result){
                is Result.Success -> {
                    database.runInTransaction {
                        database.packDao().insertPacks(result.data)
                        database.packDao().updateSavedPacks(result.data.map { it.id })
                    }
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