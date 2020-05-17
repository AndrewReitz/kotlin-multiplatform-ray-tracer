package cash.andrew.mntrailconditions.data.api

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
object ApiModule {
  val MORC_PRODUCTION_URL = "https://us-central1-mn-trail-functions.cloudfunctions.net/morcTrails".toHttpUrl()
  val PRODUCTION_API_URL = "https://mn-trail-info-service.herokuapp.com".toHttpUrl()

  @Provides
  @Singleton
  fun provideRetrofit(
    baseUrl: HttpUrl,
    client: OkHttpClient,
    moshi: Moshi
  ): Retrofit = Retrofit.Builder()
    .client(client)
    .baseUrl(baseUrl)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

  @Provides
  @Singleton
  fun provideTrailConditionsService(retrofit: Retrofit): TrailConditionsService =
    retrofit.create(TrailConditionsService::class.java)
}
