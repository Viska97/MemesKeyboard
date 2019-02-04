package org.visapps.vkstickerskeyboard.ui.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.visapps.vkstickerskeyboard.data.backend.BackendRepository
import org.visapps.vkstickerskeyboard.data.models.Pack
import org.visapps.vkstickerskeyboard.data.models.Sticker
import org.visapps.vkstickerskeyboard.util.NetworkState
import org.visapps.vkstickerskeyboard.util.Result
import kotlin.coroutines.CoroutineContext

class PackViewModel(private val repository : BackendRepository, private val packId : Int) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private val job = Job()

    val networkState = MutableLiveData<Int>()
    val pack = MutableLiveData<Pack>()
    val stickers = MutableLiveData<List<Sticker>>()

    init {
        reload()
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun reload() {
        loadPack()
        loadStickers()
    }

    private fun loadPack() {
        launch(coroutineContext){
            val result = repository.getPack(packId)
            if(result is Result.Success){
                pack.postValue(result.data)
            }
        }
    }

    private fun loadStickers() {
        launch( coroutineContext) {
            networkState.postValue(NetworkState.RUNNING)
            val result = repository.getStickers(packId)
            when(result){
                is Result.Success -> {stickers.postValue(result.data)
                    networkState.postValue(NetworkState.SUCCESS)}
                is Result.Error -> networkState.postValue(NetworkState.FAILED)
            }
        }
    }
}
