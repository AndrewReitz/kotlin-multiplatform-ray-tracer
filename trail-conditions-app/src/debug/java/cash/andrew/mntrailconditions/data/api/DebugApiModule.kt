package cash.andrew.mntrailconditions.data.api

import cash.andrew.mntrailconditions.data.ApiEndpoint
import cash.andrew.mntrailconditions.data.preference.Preference

import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import io.ktor.client.*
import javax.inject.Singleton
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import trail.networking.AggregatedTrailsRepository

@Module
object DebugApiModule {

  @Provides
  @Singleton
  fun provideTrailAggregator(
    @ApiEndpoint apiEndpoint: Preference<String>,
    httpClient: HttpClient
  ) = AggregatedTrailsRepository(httpClient, apiEndpoint.get())

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
