package cash.andrew.mntrailconditions.data

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import cash.andrew.mntrailconditions.data.api.ApiModule
import cash.andrew.mntrailconditions.data.moshi.adapters.InstantJsonAdapter
import cash.andrew.mntrailconditions.data.moshi.adapters.LocalDateTimeJsonAdapter
import cash.andrew.mntrailconditions.data.okhttp.UserAgentInterceptor
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.jakewharton.byteunits.DecimalByteUnit.MEGABYTES
import com.readystatesoftware.chuck.ChuckInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File
import javax.inject.Singleton

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

                .build()

    @JvmStatic
    @Provides
    @Singleton
    fun provideOkHttpClient(app: Application): OkHttpClient = createOkHttpClient(app).build()


    @JvmStatic
    @Provides
    @Singleton
    fun provideTrailFavorites(prefs: RxSharedPreferences) = prefs.getStringSet("trail-favorites")

    private fun createOkHttpClient(app: Application): OkHttpClient.Builder =
            OkHttpClient.Builder()
                .cache(Cache(File(app.cacheDir, "http"), DISK_CACHE_SIZE.toLong()))
                .addInterceptor(UserAgentInterceptor())
                .addInterceptor(ChuckInterceptor(app).showNotification(false))
}
