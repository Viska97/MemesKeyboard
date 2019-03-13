package org.visapps.vkmemeskeyboard.ui.keyboard

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import kotlinx.coroutines.*
import org.visapps.vkmemeskeyboard.data.backend.BackendRepository
import org.visapps.vkmemeskeyboard.data.models.Dialog
import org.visapps.vkmemeskeyboard.data.vk.VKRepository
import org.visapps.vkmemeskeyboard.util.NetworkState
import org.visapps.vkmemeskeyboard.util.Result
import kotlin.coroutines.CoroutineContext

class MemesKeyboardViewModel(private val backendRepository: BackendRepository, private val vkRepository : VKRepository)
    : ViewModel(), CoroutineScope {

    companion object {
        private const val TAG = "SKViewModel"

        private const val pageSize = 20
        private const val prefetchDistance = 5;
    }

    override val coroutineContext: CoroutineContext
        get() = viewModelJob + Dispatchers.Main

    private val viewModelJob = Job()
    private var dialogsLoadingJob : Job? = null

    private var startMessageId : Int? = null

    private val boundaryCallback = object : PagedList.BoundaryCallback<Dialog>() {
        override fun onZeroItemsLoaded() {
            startMessageId = null
            loadDialogs()
        }

        override fun onItemAtEndLoaded(itemAtEnd: Dialog) {
            startMessageId = itemAtEnd.last_message_id
            loadDialogs()
        }
    }

    private val authStatus = vkRepository.authStatus

    val keyboardState = MediatorLiveData<KeyboardState>()
    val dialogs = vkRepository.getDialogs(pageSize, prefetchDistance, boundaryCallback)
    val networkState = MutableLiveData<NetworkState>()
    val refreshState = MutableLiveData<NetworkState>()

    init{
        refreshState.postValue(null)
        networkState.postValue(null)
        keyboardState.addSource(authStatus) {updateState(it)}
    }

    public override fun onCleared() {
        viewModelJob.cancel()
        Log.i(TAG, "On cleared")
        super.onCleared()
    }

    fun refreshDialogs() {
        if(dialogsLoadingJob?.isActive == true){
            return
        }
        dialogsLoadingJob = launch(coroutineContext) {
            refreshState.postValue(NetworkState.RUNNING)
            val result = withContext(Dispatchers.IO){ vkRepository.refreshDialogs(pageSize) }
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

    fun loadDialogs() {
        if(dialogsLoadingJob?.isActive == true){
            return
        }
        dialogsLoadingJob = launch(coroutineContext) {
            networkState.postValue(org.visapps.vkmemeskeyboard.util.NetworkState.RUNNING)
            val result = withContext(Dispatchers.IO) {
                vkRepository.loadDialogs(pageSize, startMessageId)
            }
            when(result){
                is Result.Success -> {
                    networkState.postValue(NetworkState.SUCCESS)
                }
                is Result.Error -> {
                    networkState.postValue(NetworkState.FAILED)
                }
            }
        }

    }

    private fun updateState(isLoggedIn : Boolean) {
        val currentState = keyboardState.value
        if(isLoggedIn && (currentState == KeyboardState.NOT_AUTHENTICATED || currentState == null)){
            keyboardState.postValue(KeyboardState.DIALOGS)
        }
        else if(!isLoggedIn && (currentState == KeyboardState.DIALOGS || currentState == KeyboardState.STICKERS)){
            keyboardState.postValue(KeyboardState.NOT_AUTHENTICATED)
        }
    }

}