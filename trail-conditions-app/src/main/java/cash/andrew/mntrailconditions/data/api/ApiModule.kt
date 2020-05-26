package cash.andrew.mntrailconditions.data.api

import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
object ApiModule {
  @Provides
  @Singleton
  fun provideHttpClient(okHttpClient: OkHttpClient): HttpClient = HttpClient(OkHttp) {
    engine {
      preconfigured = okHttpClient
    }
  }
}
