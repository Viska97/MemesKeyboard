package org.visapps.vkmemeskeyboard.data.vk

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.vk.sdk.VKAccessToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.visapps.vkmemeskeyboard.data.database.AppDatabase
import org.visapps.vkmemeskeyboard.data.models.Dialog
import org.visapps.vkmemeskeyboard.util.NetworkState
import org.visapps.vkmemeskeyboard.util.Result

class DialogsBoundaryCallback(
    private val pageSize: Int,
    private val dataSource: VKDataSource,
    private val database: AppDatabase,
    private val logoutCallback: () -> Unit
) : PagedList.BoundaryCallback<Dialog>() {

    var networkState = MutableLiveData<Int>()

    private var isRequestInProgress = false

    override fun onZeroItemsLoaded() {
        loadDialogs(null)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Dialog) {
        loadDialogs(itemAtEnd.last_message_id)
    }

    fun reload() {
        loadDialogs(null)
    }

    private fun loadDialogs(start_message_id: Int?) {
        if(VKAccessToken.currentToken() == null) {
            return
        }
        if (isRequestInProgress) return
        isRequestInProgress = true
        GlobalScope.launch(Dispatchers.IO) {
            networkState.postValue(NetworkState.RUNNING)
            val result = dataSource.getConversations(
                VKRepository.API_VERSION,
                VKAccessToken.currentToken().accessToken,
                pageSize,
                "all",
                true,
                start_message_id,
                "photo_max"
            )
            when (result) {
                is Result.Success -> {
                    database.dialogDao().insertDialogs(
                        VKRepository.processDialogs(
                            result.data
                        )
                    )
                }
                is Result.Error -> {
                    networkState.postValue(NetworkState.FAILED)
                }
                is Result.NotAuthenticated -> {
                    logoutCallback.invoke()
                }
            }
            isRequestInProgress = false
        }
    }
}