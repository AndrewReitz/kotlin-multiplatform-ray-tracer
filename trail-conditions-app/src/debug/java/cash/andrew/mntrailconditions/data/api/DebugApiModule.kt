package cash.andrew.mntrailconditions.data.api

import cash.andrew.mntrailconditions.data.ApiEndpoint
import cash.andrew.mntrailconditions.data.ApiEndpoints
import cash.andrew.mntrailconditions.data.MORC_PRODUCTION_URL
import cash.andrew.mntrailconditions.data.HEROKU_PRODUCTION_API_URL
import cash.andrew.mntrailconditions.data.preference.Preference

import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import io.ktor.client.HttpClient
import javax.inject.Singleton
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import trail.networking.HtmlMorcTrailRepository
import trail.networking.MorcTrailRepository

@Module
object DebugApiModule {

  @Provides
  @Singleton
  fun provideMorcTrailsRepository(
    client: HttpClient,
    @ApiEndpoint apiEndpoint: Preference<String>
  ): MorcTrailRepository {
    if (ApiEndpoints.from(apiEndpoint.get()) == ApiEndpoints.CUSTOM) {
      return MorcTrailRepository(client, apiEndpoint.get())
    }

    return MorcTrailRepository(client, MORC_PRODUCTION_URL.toString())
  }

  @Provides
  @Singleton
  fun provideStuff(
    client: HttpClient,
    @ApiEndpoint apiEndpoint: Preference<String>
  ): HtmlMorcTrailRepository {
    if (ApiEndpoints.from(apiEndpoint.get()) == ApiEndpoints.CUSTOM) {
      return HtmlMorcTrailRepository(client, apiEndpoint.get())
    }

    return HtmlMorcTrailRepository(client, HEROKU_PRODUCTION_API_URL.toString())
  }

  @Provides
  @Singleton
  fun provideHttpUrl(@ApiEndpoint apiEndpoint: Preference<String>): HttpUrl = apiEndpoint.get().toHttpUrl()

  @Provides
  @IntoSet
  fun provideLoggingInterceptor(): Interceptor {
    val logger = object : HttpLoggingInterceptor.Logger {
      override fun log(message: String) {
        Timber.tag("OkHttp").v(message)
      }
    }
    val loggingInterceptor = HttpLoggingInterceptor(logger)
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return loggingInterceptor
  }

  @Provides
  @IntoSet
  fun provideStethoInterceptor(): Interceptor = StethoInterceptor()
}
