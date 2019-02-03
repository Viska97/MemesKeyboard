package org.visapps.vkstickerskeyboard.data.backend

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.visapps.vkstickerskeyboard.data.models.Pack

class SearchPayload (
    @SerializedName("search_text")
    @Expose
    val searchText: String = "",
    @SerializedName("limit")
    @Expose
    val limit: Int = 30,
    @SerializedName("offset")
    @Expose
    val offset: Int = 0
)

class SearchResponse {
    @SerializedName("result")
    @Expose
    var result: List<Pack>? = null
    @SerializedName("errorMessage")
    @Expose
    var errorMessage: String? = "fucked"
    @SerializedName("errorCode")
    @Expose
    var errorCode: Int? = 9
}