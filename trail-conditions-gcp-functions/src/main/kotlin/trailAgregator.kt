import aggregator.TrailAggregator
import io.ktor.client.HttpClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asDeferred
import kotlinx.coroutines.promise
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import trail.networking.HtmlMorcTrailRepository
import trail.networking.MorcTrailRepository
import trail.networking.model.TrailInfo
import trails.config.Keys
import kotlin.js.Date
import kotlin.js.Promise
import kotlin.time.ExperimentalTime
import kotlin.time.minutes

private val http = HttpClient()
private val newRepository = MorcTrailRepository(http)
private val oldRepository = HtmlMorcTrailRepository(http)
private val json = Json(JsonConfiguration.Stable)

data class ServiceAccount(
  val clientEmail: String,
  val privateKey: String,
  val projectId: String
)

@JsModule("firebase-admin")
@Suppress("ClassName")
external object admin {
  fun initializeApp(credentials: Credentials)
  fun firestore(): Db

  object credential {
    fun cert(serviceAccount: ServiceAccount): Cert
  }
}

data class Credentials(
  val credential: Cert
)
external class Cert
external class Collection {
  fun get(): Promise<dynamic>
}

external class Db {
  fun collection(path: String): Collection
}

data class FireStoreTrailData(
  val name: String,
  val status: String,
  val updatedAt: Date,
  val description: String
)

fun firebase() {
  val serviceAccount = ServiceAccount(
    clientEmail = Keys.firebase_client_email,
    privateKey = Keys.firebase_private_key,
    projectId = Keys.firebase_project_id
  )

  val creds = admin.credential.cert(serviceAccount)
  admin.initializeApp(Credentials(creds))
  val db = admin.firestore()
  db.collection("trailsV2")
}

@JsModule("fs")
@Suppress("ClassName")
external object fs {
  fun readFileSync(path: String, encoding: String): String
  fun existsSync(path: String): Boolean
  fun writeFileSync(path: String, data: String)
}

@Serializable
data class CachData(
  val cachedAt: Long,
  val data: List<TrailInfo> = emptyList()
)

@ExperimentalTime
fun Response.send(data: List<TrailInfo>) = json(
  data = data,
  serializer = TrailInfo.serializer().list,
  maxCacheAge = 5.minutes
)

// https://us-central1-mn-trail-functions.cloudfunctions.net/trailAggregator
@ExperimentalTime
@JsName("trailAggregator")
val trailAggregator = { _: dynamic, res: Response ->
  GlobalScope.promise {
    // /tmp is an in memory data storage on gcp
    val cache = if (fs.existsSync("/tmp/cache.json")) {
      val data = fs.readFileSync("/tmp/cache.json", "utf-8")
      json.parse(CachData.serializer(), data)
    } else CachData(0)

    val timeSince = Date.now().toLong() - cache.cachedAt
    if (timeSince <= 5.minutes.inMilliseconds) {
      res.send(cache.data)
      return@promise
    }

    val aggregator = TrailAggregator(
      morcTrailRepository = newRepository,
      htmlMorcTrailRepository = oldRepository
    )
    val trails = aggregator.aggregatedTrails()

    val newCache = CachData(
      cachedAt = Date.now().toLong(),
      data = trails
    )

    fs.writeFileSync("/tmp/cache.json", json.stringify(CachData.serializer(), newCache))
    res.send(trails)
  }
}
