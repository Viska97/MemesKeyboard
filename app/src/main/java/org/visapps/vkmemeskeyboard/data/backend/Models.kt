package org.visapps.vkmemeskeyboard.data.backend

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.visapps.vkmemeskeyboard.data.models.Pack
import org.visapps.vkmemeskeyboard.data.models.Meme

data class SearchPayload (
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

data class StickersPayload (
    val pack_id : Int
)

data class SearchResponse (
    @SerializedName("result")
    @Expose
    var result: List<Pack>,
    @SerializedName("errorMessage")
    @Expose
    var errorMessage: String?,
    @SerializedName("errorCode")
    @Expose
    var errorCode: Int?
)

data class MemesResponse (
    @SerializedName("result")
    @Expose
    var result: List<Meme>,
    @SerializedName("errorMessage")
    @Expose
    var errorMessage: String?,
    @SerializedName("errorCode")
    @Expose
    var errorCode: Int?
)