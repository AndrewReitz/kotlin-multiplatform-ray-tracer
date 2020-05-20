package aggregator.twitter

import kotlinx.coroutines.withTimeout
import cash.andrew.kotlin.common.retry
import trail.networking.twitter.TwitterAccount
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.js.Date

data class TwitterConfig(
  val consumer_key: String,
  val consumer_secret: String,
  val access_token_key: String,
  val access_token_secret: String
)

data class TwitterParams(
  val screen_name: String
) {
  val count: Int = 1
  val exclude_replies: Boolean = true
  val include_rts: Boolean = false
  val tweet_mode: String = "extended"
}


@JsModule("twitter")
external class Twitter(config: TwitterConfig) {
  fun get(path: String, params: TwitterParams, callback: (error: dynamic, tweets: dynamic, response: dynamic) -> Unit)
}

suspend fun Twitter.getTrailStatus(account: TwitterAccount): TwitterStatus {
  return retry {
    withTimeout(2000) {
      suspendCoroutine<TwitterStatus> { continuation ->
        this@getTrailStatus.get(
          "statuses/user_timeline",
          TwitterParams(account.accountName)
        ) { error, tweets, _ ->
          if (error != null) {
            continuation.resumeWithException(Exception("Error getting tweet: ${JSON.stringify(error)}"))
          }

          val tweet = tweets[0]
          val description = tweet.full_text as String
          val updatedTime = tweet.created_at as String
          continuation.resume(TwitterStatus(description, Date.parse(updatedTime).toLong()))
        }
      }
    }
  }
}

data class TwitterStatus(
  val description: String,
  val updatedTime: Long
) {
  val status: String get() = if (description.contains("closed", ignoreCase = true)) "Closed" else "Open"
}
