package trail.gcp.aggregator.twitter

import kotlinx.coroutines.withTimeout
import cash.andrew.kotlin.common.retry
import trail.networking.twitter.TwitterAccount
import twitter4j.Paging
import twitter4j.Twitter

suspend fun Twitter.getTrailStatus(account: TwitterAccount): TwitterStatus {
  return retry {
    withTimeout(2000) {

      val tweets = this@getTrailStatus.getUserTimeline(account.accountName, Paging(1, 1))
      val tweet = tweets[0]

      val description = tweet.text
      val updatedTime = tweet.createdAt

      TwitterStatus(description, updatedTime.toInstant().toEpochMilli())
    }
  }
}

data class TwitterStatus(
  val description: String,
  val updatedTime: Long
) {
  val status: String get() = if (description.contains("closed", ignoreCase = true)) "Closed" else "Open"
}
