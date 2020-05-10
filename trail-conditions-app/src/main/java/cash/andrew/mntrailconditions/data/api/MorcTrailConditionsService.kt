package cash.andrew.mntrailconditions.data.api

import retrofit2.http.GET

interface MorcTrailConditionsService {
    @GET("/")
    suspend fun trailInfo(): List<Unit>
}