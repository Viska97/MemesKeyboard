package org.visapps.vkstickerskeyboard.ui.base

interface BaseContract {

    interface Presenter<in T> {
        fun detachView()
        fun attach(view: T)
    }

    interface View {

    }
}