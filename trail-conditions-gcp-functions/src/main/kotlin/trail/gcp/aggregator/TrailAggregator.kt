package trail.gcp.aggregator

import cash.andrew.kotlin.common.doOnFailure
import cash.andrew.kotlin.common.map
import cash.andrew.kotlin.common.recover
import cash.andrew.kotlin.common.resultFrom
import cash.andrew.kotlin.common.retry
import dagger.Reusable
import kotlinx.coroutines.*
import trail.gcp.aggregator.twitter.getTrailStatus
import trail.networking.MorcTrailRepository
import trail.networking.model.KnownTrail
import trail.networking.model.TrailInfo
import twitter4j.Twitter
import javax.inject.Inject

@Reusable
class TrailAggregator(
  private val scope: CoroutineScope,
  private val morcTrailRepository: MorcTrailRepository,
  private val twitter: Twitter
) {

  @Inject
  constructor(
    morcTrailRepository: MorcTrailRepository,
    twitter: Twitter
  ): this(
    scope = MainScope(),
    morcTrailRepository = morcTrailRepository,
    twitter = twitter
  )

  suspend fun aggregatedTrails(): List<TrailInfo> = listOf(
      suspend { listOf(cyunaTrails()) },
      suspend { coggsTrails() },
      suspend { morcTrails() }
    ).map { scope.async(Dispatchers.Default) { it() } }
      .awaitAll()
      .flatten()
      .sortedBy { it.name }

  private suspend fun morcTrails(): List<TrailInfo> = newMorcApi()
    .doOnFailure { println(it.message) }
    .recover {
      KnownTrail.morcTrails.map { TrailInfo.createUnknownStatus(it) }
    }

  private suspend fun newMorcApi() = morcTrailRepository.getTrails(timeout = 10000).map { trails ->
    val trailInfos = trails.map trailMap@{ trail ->
      val knownTrail = KnownTrail.morcTrails.find {
        it.trailName == trail.trailName || it.otherNames.contains(trail.trailName)
      }

      if (knownTrail == null) return@trailMap null

      TrailInfo(
        name = knownTrail.trailName,
        mtbProjectUrl = knownTrail.mtbProjectUrl,
        facebookUrl = knownTrail.facebookUrl,
        description = trail.description.orEmpty(),
        lastUpdated = trail.updatedAt,
        status = trail.trailStatus.orEmpty(),
        twitterAccount = knownTrail.twitterAccount?.accountName
      )
    }

    trailInfos.filterNotNull()
  }

  private suspend fun coggsTrails(): List<TrailInfo> {
    return KnownTrail.coggsTrails
      .map { knownTrail ->
        scope.async(Dispatchers.Default) {
          loadTrailInfoFromTwitter(knownTrail)
        }
      }
      .map { it.await() }
  }

  private suspend fun cyunaTrails(): TrailInfo = loadTrailInfoFromTwitter(KnownTrail.Cuyuna)

  private suspend fun loadTrailInfoFromTwitter(trail: KnownTrail): TrailInfo = resultFrom {
    val status = retry {
      withTimeout(2000) {
        twitter.getTrailStatus(requireNotNull(trail.twitterAccount))
      }
    }
    TrailInfo(
      name = trail.trailName,
      status = status.status,
      lastUpdated = status.updatedTime,
      description = status.description,
      facebookUrl = trail.facebookUrl,
      mtbProjectUrl = trail.mtbProjectUrl,
      twitterAccount = trail.twitterAccount?.accountName
    )
  }.doOnFailure { println(it.message) }
    .recover { TrailInfo.createUnknownStatus(trail) }
}
