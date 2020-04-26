package cash.andrew.mntrailconditions.data.api

import cash.andrew.mntrailconditions.data.api.ApiModule.PRODUCTION_API_URL
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
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
    @ApiClient
    fun provideApiClient(client: OkHttpClient): OkHttpClient = ApiModule.createApiClient(client).build()
}
