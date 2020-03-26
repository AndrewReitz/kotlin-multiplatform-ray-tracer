package cash.andrew.mntrailconditions.data.api

import cash.andrew.mntrailconditions.data.ApiEndpoint

import com.f2prateek.rx.preferences2.Preference
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

@Module
object DebugApiModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideHttpUrl(@ApiEndpoint apiEndpoint: Preference<String>): HttpUrl = apiEndpoint.get().toHttpUrl()

    @JvmStatic
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = object: HttpLoggingInterceptor.Logger {
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
    @Singleton
    @ApiClient
    fun provideApiClient(
            client: OkHttpClient,
            loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = ApiModule.createApiClient(client)
                .addInterceptor(loggingInterceptor)
                .addNetworkInterceptor(StethoInterceptor())
                .build()
}
