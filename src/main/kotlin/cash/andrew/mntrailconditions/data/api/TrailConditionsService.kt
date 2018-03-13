package cash.andrew.mntrailconditions.data.api

import cash.andrew.mntrailconditions.data.model.TrailData
import cash.andrew.mntrailconditions.data.model.TrailRegion
import io.reactivex.Single
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.GET
import retrofit2.http.Headers

interface TrailConditionsService {
    @GET("/?api-version=1")
    @Headers("api-version: 1")
    fun trailRegions(): Single<Result<List<TrailRegion>>>

    @GET("/?api-version=3")
    @Headers("api-version: 3")
    fun trailData(): Single<Result<List<TrailData>>>
}
