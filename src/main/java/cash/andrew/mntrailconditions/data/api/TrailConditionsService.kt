package cash.andrew.mntrailconditions.data.api

import cash.andrew.mntrailconditions.data.model.TrailDataV3
import cash.andrew.mntrailconditions.data.model.TrailDataV2
import io.reactivex.Single
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.GET
import retrofit2.http.Headers

interface TrailConditionsService {
    @GET("/?api-version=2")
    @Headers("api-version: 2")
    fun trailRegionsV2(): Single<Result<List<TrailDataV2>>>

    @GET("/?api-version=3")
    @Headers("api-version: 3")
    fun trailDataV3(): Single<Result<List<TrailDataV3>>>
}
