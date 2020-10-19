package trail.gcp

import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.ktor.client.*
import trail.networking.AggregatedTrailsRepository
import trail.networking.MorcTrailRepository
import trails.config.Keys
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder
import kotlin.time.ExperimentalTime

@ExperimentalTime
val gcpTrails: GcpTrails = DaggerGcpTrails.create()

@ExperimentalTime
@Component(modules = [GcpModule::class])
interface GcpTrails {
  fun inject(trailAggregator: TrailAggregatorFunction)
  fun inject(trailNotifications: TrailNotificationsFunction)
}

@Module
class GcpModule {

  @Provides
  @Reusable
  fun providesHttpClient() = HttpClient()

  @Provides
  @Reusable
  fun provideMorcTrailsRepository(httpClient: HttpClient) =
    MorcTrailRepository(httpClient)

  @Provides
  @Reusable
  fun provideAggregatedTrailsRepository(httpClient: HttpClient) =
    AggregatedTrailsRepository(httpClient)

  @Provides
  @Reusable
  fun provideTwitter(): Twitter {
    val config = ConfigurationBuilder()
      .setOAuthConsumerKey(Keys.consumer_key)
      .setOAuthConsumerSecret(Keys.consumer_secret)
      .setOAuthAccessToken(Keys.access_token_key)
      .setOAuthAccessTokenSecret(Keys.access_token_secret)
      .build()

    return TwitterFactory(config).instance
  }

  @Provides
  @Reusable
  fun providesFirebaseMessaging(): FirebaseMessaging {

    val serviceAccount = ServiceAccountCredentials.fromPkcs8(
      Keys.firebase_project_id,
      Keys.firebase_client_email,
      Keys.firebase_private_key,
      null,
      emptyList()
    )

    FirebaseApp.initializeApp(
      FirebaseOptions.builder()
        .setCredentials(serviceAccount)
        .setProjectId(Keys.firebase_project_id)
        .setServiceAccountId(Keys.firebase_client_email)
        .build()
    )

    return FirebaseMessaging.getInstance()
  }
}
