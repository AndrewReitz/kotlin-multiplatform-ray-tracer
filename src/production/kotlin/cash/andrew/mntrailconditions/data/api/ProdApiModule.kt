package cash.andrew.mntrailconditions.data.api

@Module
object ProdApiModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideBaseUrl(): HttpUrl = PRODUCTION_API_URL

    @JvmStatic
    @Provides
    @Singleton
    @Named("Api")
    fun provideApiClient(client: OkHttpClient): OkHttpClient = ApiModule.createApiClient(client).build()
}
