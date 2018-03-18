package cash.andrew.mntrailconditions.data

import android.content.Context.MODE_PRIVATE
import com.jakewharton.byteunits.DecimalByteUnit.MEGABYTES

import android.app.Application
import android.content.SharedPreferences
import cash.andrew.mntrailconditions.data.api.ApiModule
import cash.andrew.mntrailconditions.data.moshi.adapters.InstantJsonAdapter
import cash.andrew.mntrailconditions.data.moshi.adapters.LocalDateTimeJsonAdapter
import cash.andrew.mntrailconditions.data.okhttp.UserAgentInterceptor

import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import java.io.File
import javax.inject.Singleton
import okhttp3.Cache
import okhttp3.OkHttpClient

@Module(includes = [ApiModule::class])
object DataModule {

    private val DISK_CACHE_SIZE = MEGABYTES.toBytes(50).toInt()

    @JvmStatic
    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application): SharedPreferences =
            app.getSharedPreferences("cash.andrew.mntrailconditions", MODE_PRIVATE)

    @JvmStatic
    @Provides
    @Singleton
    fun provideRxSharedPreferences(prefs: SharedPreferences) = RxSharedPreferences.create(prefs)

    @JvmStatic
    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
                .add(InstantJsonAdapter())
                .add(LocalDateTimeJsonAdapter())
                .add(KotlinJsonAdapterFactory())
                .build()

    @JvmStatic
    @Provides
    @Singleton
    fun provideOkHttpClient(app: Application): OkHttpClient = createOkHttpClient(app).build()

    private fun createOkHttpClient(app: Application): OkHttpClient.Builder =
            OkHttpClient.Builder()
                .cache(Cache(File(app.cacheDir, "http"), DISK_CACHE_SIZE.toLong()))
                .addInterceptor(UserAgentInterceptor())
}
