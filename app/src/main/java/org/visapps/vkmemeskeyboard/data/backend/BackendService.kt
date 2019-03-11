package org.visapps.vkmemeskeyboard.data.backend

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface BackendService {

    @POST("searchPacks")
    fun searchPacks(@Body payload : SearchPayload) : Deferred<Response<SearchResponse>>

    @POST("getStickers")
    fun getMemes(@Body payload : StickersPayload) : Deferred<Response<MemesResponse>>

}