package cash.andrew.mntrailconditions.data.api

import cash.andrew.mntrailconditions.data.api.ApiModule.MORC_PRODUCTION_URL
import cash.andrew.mntrailconditions.data.api.ApiModule.PRODUCTION_API_URL
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import okhttp3.HttpUrl
import trail.networking.MorcTrailRepository
import javax.inject.Singleton

@Module
object ProdApiModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideBaseUrl(): HttpUrl = PRODUCTION_API_URL

    @JvmStatic
    @Provides
    @Singleton
    fun provideMorcTrailsRepository() = MorcTrailRepository(HttpClient(), MORC_PRODUCTION_URL.toString())
}
