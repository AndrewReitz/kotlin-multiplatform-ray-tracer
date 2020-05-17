package cash.andrew.mntrailconditions.data

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import cash.andrew.mntrailconditions.data.okhttp.UserAgentInterceptor
import cash.andrew.mntrailconditions.data.preference.Preference
import cash.andrew.mntrailconditions.data.preference.stringSetPreference
import com.jakewharton.byteunits.DecimalByteUnit.MEGABYTES
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.multibindings.ElementsIntoSet
import okhttp3.Cache
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.io.File
import javax.inject.Qualifier
import javax.inject.Singleton

val MORC_PRODUCTION_URL = "https://us-central1-mn-trail-functions.cloudfunctions.net/morcTrails".toHttpUrl()
val HEROKU_PRODUCTION_API_URL = "https://mn-trail-info-service.herokuapp.com".toHttpUrl()

const val SHARED_PREF_FILE_NAME = "cash.andrew.mntrailconditions"

@Module
object DataModule {

    private val DISK_CACHE_SIZE = MEGABYTES.toBytes(50).toInt()

    @Provides
    @Reusable
    fun provideSharedPreferences(app: Application): SharedPreferences =
        app.getSharedPreferences(SHARED_PREF_FILE_NAME, MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideOkHttpClient(app: Application, interceptors: Set<@JvmSuppressWildcards Interceptor>): OkHttpClient =
        OkHttpClient.Builder()
            .cache(Cache(File(app.cacheDir, "http"), DISK_CACHE_SIZE.toLong()))
            .apply { interceptors.forEach { addInterceptor(it) } }
            .build()

    @Provides
    @Reusable
    @ElementsIntoSet
    fun provideMainInterceptors(app: Application): Set<@JvmSuppressWildcards Interceptor> = setOf(
        UserAgentInterceptor(),
        ChuckInterceptor(app).showNotification(true)
    )

    @SavedTrails
    @Provides
    @Reusable
    fun provideTrailFavorites(prefs: SharedPreferences): Preference<Set<String>> =
        prefs.stringSetPreference("trail-favorites")

    @Provides
    @NotificationTrails
    @Reusable
    fun provideNotificationList(prefs: SharedPreferences): Preference<Set<String>> =
        prefs.stringSetPreference("trail-notifications")
}

@Qualifier annotation class SavedTrails
@Qualifier annotation class NotificationTrails
