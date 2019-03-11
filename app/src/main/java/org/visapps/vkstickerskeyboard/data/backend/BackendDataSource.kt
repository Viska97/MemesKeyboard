package org.visapps.vkstickerskeyboard.data.backend

import org.visapps.vkstickerskeyboard.data.models.Pack
import org.visapps.vkstickerskeyboard.data.models.Sticker
import org.visapps.vkstickerskeyboard.util.Result
import org.visapps.vkstickerskeyboard.util.safeApiCall
import java.io.IOException

class BackendDataSource(private val service: BackendService) {

    suspend fun searchPacks(searchText: String, limit: Int, offset: Int) =
        safeApiCall { requestSearch(searchText, limit, offset) }

    suspend fun getStickers(packId: Int) = safeApiCall { requestStickers(packId) }

    private suspend fun requestSearch(searchText: String, limit: Int, offset: Int): Result<List<Pack>> {
        val response = service.searchPacks(SearchPayload(searchText, limit, offset)).await()
        if (response.isSuccessful) {
            response.body()?.result?.let {
                return Result.Success(it)
            }
        }
        return Result.Error(
            IOException("Error getting data")
        )
    }

    private suspend fun requestStickers(packId: Int): Result<List<Sticker>> {
        val response = service.getStickers(StickersPayload(packId)).await()
        if (response.isSuccessful) {
            response.body()?.result?.let {
                return Result.Success(it)
            }
        }
        return Result.Error(
            IOException("Error getting data")
        )
    }
}