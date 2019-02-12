package org.visapps.vkstickerskeyboard.data.backend

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import kotlinx.coroutines.*
import org.visapps.vkstickerskeyboard.data.database.AppDatabase
import org.visapps.vkstickerskeyboard.data.models.Pack
import org.visapps.vkstickerskeyboard.util.NetworkState
import org.visapps.vkstickerskeyboard.util.Result

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
            delay(1000L)
            val offset = database.packDao().packsCount()
            val result = dataSource.searchPacks(searchText, pageSize, offset)
            when(result){
                is Result.Success -> {
                    database.packDao().addPacks(result.data)
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