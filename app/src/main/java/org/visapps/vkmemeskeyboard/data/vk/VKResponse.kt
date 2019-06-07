package org.visapps.vkmemeskeyboard.data.vk

abstract class VKResponse<T> {
    var response: T? = null
    var error: VKError? = null
}