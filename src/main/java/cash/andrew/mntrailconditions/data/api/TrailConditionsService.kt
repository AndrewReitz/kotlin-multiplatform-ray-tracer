package cash.andrew.mntrailconditions.data.api

import cash.andrew.mntrailconditions.data.model.TrailData
import retrofit2.http.GET
import retrofit2.http.Headers

interface TrailConditionsService {
    @GET("/")
    @Headers("api-version: 2")
    suspend fun trailRegions(): List<TrailData>
}
