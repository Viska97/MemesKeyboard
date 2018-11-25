package org.visapps.vkstickerskeyboard.ui.keyboard

import org.visapps.vkstickerskeyboard.data.models.Chat
import org.visapps.vkstickerskeyboard.data.vk.ConversationsResponse
import org.visapps.vkstickerskeyboard.ui.base.BaseContract

class StickersContract {

    interface View: BaseContract.View {
        fun updateLoginStatus(loggedIn: Boolean)
        fun updateChats(chats : ConversationsResponse)
        fun clearChats()
        fun updateLoadingState(isLoading : Boolean)
        fun showError(message : String)
    }

    interface Presenter: BaseContract.Presenter<StickersContract.View> {
        fun loadChats()
    }
}