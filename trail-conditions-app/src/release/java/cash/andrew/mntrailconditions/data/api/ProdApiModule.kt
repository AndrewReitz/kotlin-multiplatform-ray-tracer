package cash.andrew.mntrailconditions.data.api

import cash.andrew.mntrailconditions.data.MORC_PRODUCTION_URL
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.OkHttpClient
import trail.networking.AggregatedTrailsRepository
import trail.networking.MorcTrailRepository
import javax.inject.Singleton

@Module
object ProdApiModule {

  @Provides
  @Singleton
  fun provideTrailAggregator(httpClient: HttpClient) = AggregatedTrailsRepository(httpClient)

  @Provides
  @Singleton
  fun provideMorcTrailsRepository(okHttpClient: OkHttpClient) = MorcTrailRepository(
    HttpClient(OkHttp) {
      engine {
        preconfigured = okHttpClient
      }
    }, MORC_PRODUCTION_URL.toString()
  )
}
