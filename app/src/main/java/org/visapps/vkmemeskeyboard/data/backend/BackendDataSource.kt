package org.visapps.vkmemeskeyboard.data.backend

import org.visapps.vkmemeskeyboard.data.models.Pack
import org.visapps.vkmemeskeyboard.data.models.Meme
import org.visapps.vkmemeskeyboard.util.Result
import org.visapps.vkmemeskeyboard.util.safeApiCall
import java.io.IOException

class BackendDataSource(private val service: BackendService) {

    suspend fun searchPacks(searchText: String, limit: Int, offset: Int): Result<List<Pack>> = safeApiCall {
        val response = service.searchPacksAsync(
            SearchPayload(
                searchText,
                limit,
                offset
            )
        ).await()
        if (response.isSuccessful) {
            response.body()?.result?.let {
                return@safeApiCall Result.Success(it)
            }
        }
        return@safeApiCall Result.Error(
            IOException("Error getting data")
        )
    }

    suspend fun getMemes(packId: Int): Result<List<Meme>> = safeApiCall {
        val response = service.getMemesAsync(MemesPayload(packId)).await()
        if (response.isSuccessful) {
            response.body()?.result?.let {
                return@safeApiCall Result.Success(it)
            }
        }
        return@safeApiCall Result.Error(
            IOException("Error getting data")
        )
    }
}