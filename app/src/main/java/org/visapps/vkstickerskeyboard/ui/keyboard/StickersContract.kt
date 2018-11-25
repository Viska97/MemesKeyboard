package org.visapps.vkstickerskeyboard.ui.keyboard

import org.visapps.vkstickerskeyboard.ui.base.BaseContract

class StickersContract {

    interface View: BaseContract.View {
        fun updateLoginStatus(boolean: Boolean)
    }

    interface Presenter: BaseContract.Presenter<StickersContract.View> {

    }
}