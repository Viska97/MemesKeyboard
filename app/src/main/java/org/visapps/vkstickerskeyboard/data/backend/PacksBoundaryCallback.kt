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
    private val database : AppDatabase,
    private var job : Job
) : PagedList.BoundaryCallback<Pack>(){

    var networkState = MutableLiveData<Int>()

    private var lastOffset = 0

    override fun onZeroItemsLoaded() {
        Log.i("Vasily", "onZeroItemsLoaded")
        load(0)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Pack) {
        load(itemAtEnd.id)
    }

    fun reload() {
        load(lastOffset)
    }

    private fun load(offset : Int) {
        Log.i("Vasily", "request loading")
        if(!job.isActive){
            lastOffset = offset
            job = GlobalScope.launch(Dispatchers.IO) {
                Log.i("Vasily", "loading")
                networkState.postValue(NetworkState.RUNNING)
                val result = dataSource.searchPacks(searchText, pageSize, offset)
                when(result){
                    is Result.Success -> {
                        Log.i("Vasily", result.data.size.toString())
                        database.packDao().insertPacks(result.data)
                        networkState.postValue(NetworkState.SUCCESS)
                    }
                    is Result.Error -> {
                        networkState.postValue(NetworkState.FAILED)
                    }
                }
            }
        }
    }

}