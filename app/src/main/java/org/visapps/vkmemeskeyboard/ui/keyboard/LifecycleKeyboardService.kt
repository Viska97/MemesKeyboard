package org.visapps.vkmemeskeyboard.ui.keyboard

import android.inputmethodservice.InputMethodService
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.*

abstract class LifecycleKeyboardService : InputMethodService(), LifecycleOwner {

    private val registry = LifecycleRegistry(this)

    override fun onCreate() {
        registry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        super.onCreate()
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        registry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        super.onStartInputView(info, restarting)
    }

    override fun onFinishInput() {
        registry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        super.onFinishInput()
    }

    override fun onDestroy() {
        registry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        super.onDestroy()
    }

    override fun getLifecycle(): Lifecycle {
        return registry
    }
}