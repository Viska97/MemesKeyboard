package org.visapps.vkstickerskeyboard.ui.base

interface BaseContract {

    interface Presenter<in T> {
        fun onStart()
        fun onStop()
        fun detachView()
        fun attach(view: T)
    }

    interface View {

    }
}