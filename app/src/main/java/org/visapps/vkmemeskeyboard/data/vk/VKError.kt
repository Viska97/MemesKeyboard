package org.visapps.vkmemeskeyboard.data.vk

import com.google.gson.annotations.SerializedName

data class VKError(
    @SerializedName("error")
    val body : ErrorBody
)

data class ErrorBody(
    @SerializedName("error_code")
    val errorCode : Int,
    @SerializedName("error_msg")
    val errorMsg : String,
    @SerializedName("request_params")
    val requestParams : List<RequestParam>
)

data class RequestParam(
    val key : String,
    val value : String
)

