package cash.andrew.mntrailconditions.data.api

import cash.andrew.kotlin.common.Result
import cash.andrew.kotlin.common.resultFrom
import cash.andrew.kotlin.common.retry
import cash.andrew.mntrailconditions.ui.trails.TrailViewModel
import cash.andrew.mntrailconditions.ui.trails.toViewModel
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrailConditionsRepo @Inject constructor(
        private val trailConditionsService: TrailConditionsService
) {
    suspend fun getOriginalTrailData(): Result<List<TrailViewModel>, Exception> = resultFrom {
        retry {
            withTimeout(3000) { trailConditionsService.trailRegions() }
        }.map { it.toViewModel() }.sortedBy { it.name }
    }
}
