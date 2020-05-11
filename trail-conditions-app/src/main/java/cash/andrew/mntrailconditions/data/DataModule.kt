package cash.andrew.mntrailconditions.data

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import cash.andrew.mntrailconditions.data.api.ApiModule
import cash.andrew.mntrailconditions.data.moshi.adapters.InstantJsonAdapter
import cash.andrew.mntrailconditions.data.moshi.adapters.LocalDateTimeJsonAdapter
import cash.andrew.mntrailconditions.data.okhttp.UserAgentInterceptor
import cash.andrew.mntrailconditions.data.preference.Preference
import cash.andrew.mntrailconditions.data.preference.stringSetPreference
import com.jakewharton.byteunits.DecimalByteUnit.MEGABYTES
import com.readystatesoftware.chuck.ChuckInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File
import javax.inject.Qualifier
import javax.inject.Singleton

const val SHARED_PREF_FILE_NAME = "cash.andrew.mntrailconditions"

@Module(includes = [ApiModule::class])
object DataModule {

    private val DISK_CACHE_SIZE = MEGABYTES.toBytes(50).toInt()

    @JvmStatic
    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application): SharedPreferences =
            app.getSharedPreferences(SHARED_PREF_FILE_NAME, MODE_PRIVATE)

    @JvmStatic
    @Provides
    @Singleton
    fun provideMoshiBuilder(): Moshi.Builder = Moshi.Builder()
                .add(InstantJsonAdapter())
                .add(LocalDateTimeJsonAdapter())

    @JvmStatic
    @Provides
    @Singleton
    fun provideOkHttpClient(app: Application): OkHttpClient = createOkHttpClient(app).build()

    @JvmStatic
    @SavedTrails
    @Provides
    @Singleton
    fun provideTrailFavorites(prefs: SharedPreferences): Preference<Set<String>> =
            prefs.stringSetPreference( "trail-favorites")

    @JvmStatic
    @Provides
    @NotificationTrails
    @Singleton
    fun provideNotificationList(prefs: SharedPreferences): Preference<Set<String>> =
            prefs.stringSetPreference( "trail-notifications")

    private fun createOkHttpClient(app: Application): OkHttpClient.Builder =
            OkHttpClient.Builder()
                .cache(Cache(File(app.cacheDir, "http"), DISK_CACHE_SIZE.toLong()))
                .addInterceptor(UserAgentInterceptor())
                .addInterceptor(ChuckInterceptor(app).showNotification(false))
}

@Qualifier annotation class SavedTrails
@Qualifier annotation class NotificationTrails