package org.visapps.vkmemeskeyboard.ui.keyboard

import androidx.lifecycle.*
import androidx.paging.PagedList
import kotlinx.coroutines.*
import org.visapps.vkmemeskeyboard.data.backend.BackendRepository
import org.visapps.vkmemeskeyboard.data.models.Dialog
import org.visapps.vkmemeskeyboard.data.vk.VKRepository
import org.visapps.vkmemeskeyboard.util.AbsentLiveData
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
            loadInitialDialogs()
        }

        override fun onItemAtEndLoaded(itemAtEnd: Dialog) {
            startMessageId = itemAtEnd.last_message_id
            loadNextDialogs()
        }
    }

    private val isLoggedIn = vkRepository.isLoggedIn

    val keyboardState = MediatorLiveData<KeyboardState>()
    val dialogs: LiveData<PagedList<Dialog>> = Transformations.switchMap(keyboardState) { state ->
        if (state == KeyboardState.DIALOGS) {
            vkRepository.getDialogs(pageSize, prefetchDistance, boundaryCallback)
        } else {
            AbsentLiveData.create()
        }
    }
    val networkState = MutableLiveData<NetworkState>()
    val refreshState = MutableLiveData<NetworkState>()

    init{
        refreshState.postValue(NetworkState.SUCCESS)
        networkState.postValue(NetworkState.SUCCESS)
        keyboardState.addSource(isLoggedIn) {updateState(it)}
    }

    public override fun onCleared() {
        viewModelJob.cancel()
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

    fun retryLoading() = loadNextDialogs()

    private fun loadInitialDialogs() {
        if(dialogsLoadingJob?.isActive == true){
            return
        }
        dialogsLoadingJob = launch(coroutineContext) {
            refreshState.postValue(NetworkState.RUNNING)
            val result = withContext(Dispatchers.IO) {
                vkRepository.loadDialogs(pageSize)
            }
            when(result){
                is Result.Success -> {
                    refreshState.postValue(NetworkState.SUCCESS)
                }
                is Result.Error -> {
                    refreshState.postValue(NetworkState.FAILED)
                }
            }
        }
    }

    private fun loadNextDialogs() {
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