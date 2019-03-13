package org.visapps.vkmemeskeyboard.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import kotlinx.coroutines.*
import org.visapps.vkmemeskeyboard.data.backend.BackendRepository
import org.visapps.vkmemeskeyboard.data.models.Pack
import org.visapps.vkmemeskeyboard.data.models.Meme
import org.visapps.vkmemeskeyboard.util.NetworkState
import org.visapps.vkmemeskeyboard.util.Result
import kotlin.coroutines.CoroutineContext

class PackViewModel(private val repository : BackendRepository, private val packId : Int) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private val job = Job()

    val networkState = MutableLiveData<NetworkState>()
    val pack = MutableLiveData<Pack>()
    val stickers = MutableLiveData<List<Meme>>()

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
            val result = withContext(Dispatchers.IO) {repository.getPack(packId)}
            if(result is Result.Success){
                pack.postValue(result.data)
            }
        }
    }

    private fun loadStickers() {
        launch( coroutineContext) {
            networkState.postValue(NetworkState.RUNNING)
            val result = withContext(Dispatchers.IO) {repository.getMemes(packId)}
            when(result){
                is Result.Success -> {stickers.postValue(result.data)
                    networkState.postValue(NetworkState.SUCCESS)}
                is Result.Error -> networkState.postValue(NetworkState.FAILED)
            }
        }
    }
}
