package aggregator

import aggregator.twitter.Twitter
import aggregator.twitter.TwitterConfig
import aggregator.twitter.getTrailStatus
import cash.andrew.kotlin.common.*
import kotlinx.coroutines.*
import trail.networking.HtmlMorcTrailRepository
import trail.networking.MorcTrailRepository
import trail.networking.model.KnownTrail
import trail.networking.model.TrailInfo
import trails.config.Keys
import kotlin.js.Date

class TrailAggregator(
  private val scope: CoroutineScope = MainScope(),
  private val morcTrailRepository: MorcTrailRepository,
  private val htmlMorcTrailRepository: HtmlMorcTrailRepository,
  private val twitter: Twitter = Twitter(
    config = TwitterConfig(
      consumer_key = Keys.consumer_key,
      consumer_secret = Keys.consumer_secret,
      access_token_key = Keys.access_token_key,
      access_token_secret = Keys.access_token_secret
    )
  )
) {

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
    .flatMapFailure { oldMorcApi() }
    .doOnFailure { println(it.message) }
    .recover {
      KnownTrail.morcTrails.map { TrailInfo.createUnknownStatus(it) }
    }

  private suspend fun newMorcApi() = morcTrailRepository.getTrails().map { trails ->
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
        status = trail.trailStatus.orEmpty()
      )
    }

    trailInfos.filterNotNull()
  }

  private suspend fun oldMorcApi() = htmlMorcTrailRepository.getTrails().map { trails ->
    val trailInfos = trails.map trailsMap@{ trail ->
      val knownTrail = KnownTrail.morcTrails.find {
        it.trailName == trail.name || it.otherNames.contains(trail.name)
      }

      if (knownTrail == null) return@trailsMap null

      TrailInfo(
        name = knownTrail.trailName,
        mtbProjectUrl = knownTrail.mtbProjectUrl,
        facebookUrl = knownTrail.facebookUrl,
        description = trail.fullDescription,
        lastUpdated = Date.parse(trail.lastUpdated).toLong(),
        status = trail.status
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
      mtbProjectUrl = trail.mtbProjectUrl
    )
  }.doOnFailure { println(it.message) }
    .recover { TrailInfo.createUnknownStatus(trail) }
}
