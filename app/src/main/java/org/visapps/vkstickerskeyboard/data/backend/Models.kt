package org.visapps.vkstickerskeyboard.data.backend

import com.google.gson.annotations.SerializedName
import org.visapps.vkstickerskeyboard.data.models.Pack

data class SearchPayload(
    @SerializedName("search_text")
    val searchText: String = "",
    @SerializedName("limit")
    val limit: Int = 30,
    @SerializedName("offset")
    val offset: Int = 0
)

data class SearchResponse(
    @SerializedName("result")
    val result: List<Pack>,
    @SerializedName("errorMessage")
    val errorMessage : String
)