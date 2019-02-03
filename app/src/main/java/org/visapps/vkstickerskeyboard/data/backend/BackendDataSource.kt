package org.visapps.vkstickerskeyboard.data.backend

import android.util.Log
import org.visapps.vkstickerskeyboard.data.models.Pack
import org.visapps.vkstickerskeyboard.util.Result
import org.visapps.vkstickerskeyboard.util.safeApiCall
import java.io.IOException

class BackendDataSource(private val service: BackendService) {

    suspend fun searchPacks(searchText: String, limit: Int, offset: Int) =
        safeApiCall { requestSearch(searchText, limit, offset) }

    private suspend fun requestSearch(searchText: String, limit: Int, offset: Int): Result<List<Pack>> {
        Log.i("Vasily", "start netwrok request")
        val response = service.searchPacks(SearchPayload(searchText, limit, offset)).await()
        Log.i("Vasily", response.code().toString())
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Log.i("Vasily", "here something wrong")
                Log.i("Vasily", body.errorMessage)
                return Result.Success(body.result)
            }
        }
        return Result.Error(
            IOException("Error getting data")
        )
    }
}