package cash.andrew.mntrailconditions.data.api

import cash.andrew.mntrailconditions.data.ApiEndpoint
import cash.andrew.mntrailconditions.data.ApiEndpoints
import cash.andrew.mntrailconditions.data.api.ApiModule.MORC_PRODUCTION_URL
import cash.andrew.mntrailconditions.data.preference.Preference

import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import javax.inject.Singleton
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import trail.networking.MorcTrailRepository

@Module
object DebugApiModule {

  @JvmStatic
  @Provides
  @Singleton
  fun provideMorcTrailsRepository(
    okHttpClient: OkHttpClient,
    @ApiEndpoint apiEndpoint: Preference<String>
  ): MorcTrailRepository {
    val client = HttpClient(OkHttp) {
      engine {
        preconfigured = okHttpClient
      }
    }

    if (ApiEndpoints.from(apiEndpoint.get()) == ApiEndpoints.CUSTOM) {
      return MorcTrailRepository(client, apiEndpoint.get())
    }

    return MorcTrailRepository(client, MORC_PRODUCTION_URL.toString())
  }

  @JvmStatic
  @Provides
  @Singleton
  fun provideHttpUrl(@ApiEndpoint apiEndpoint: Preference<String>): HttpUrl = apiEndpoint.get().toHttpUrl()

  @JvmStatic
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

  @JvmStatic
  @Provides
  @IntoSet
  fun provideStethoInterceptor(): Interceptor = StethoInterceptor()
}
