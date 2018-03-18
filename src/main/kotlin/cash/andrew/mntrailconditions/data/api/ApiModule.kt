package cash.andrew.mntrailconditions.data.api

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier

@Qualifier annotation class ApiClient

@Module object ApiModule {

    val PRODUCTION_API_URL = HttpUrl.parse("https://mn-trail-info-service.herokuapp.com")!!

    @JvmStatic
    @Provides
    @Singleton
    fun provideRetrofit(
            baseUrl: HttpUrl,
            @ApiClient client: OkHttpClient,
            moshi: Moshi
    ): Retrofit = Retrofit.Builder() //
                .client(client) //
                .baseUrl(baseUrl) //
                .addConverterFactory(MoshiConverterFactory.create(moshi)) //
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync()) //
                .build()

    @JvmStatic
    @Provides
    @Singleton
    fun provideTrailConditionsService(retrofit: Retrofit): TrailConditionsService =
            retrofit.create(TrailConditionsService::class.java)

    fun createApiClient(client: OkHttpClient): OkHttpClient.Builder =  client.newBuilder()
}
