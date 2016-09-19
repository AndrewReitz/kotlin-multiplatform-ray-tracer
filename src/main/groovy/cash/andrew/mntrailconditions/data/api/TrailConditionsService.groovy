package cash.andrew.mntrailconditions.data.api

import cash.andrew.mntrailconditions.data.model.TrailRegion
import retrofit2.adapter.rxjava.Result
import rx.Observable
import retrofit2.http.GET

interface TrailConditionsService {
  @GET('/') Observable<Result<List<TrailRegion>>> getTrailRegions()
}
