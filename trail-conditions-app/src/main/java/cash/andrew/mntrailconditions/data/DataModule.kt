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
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.multibindings.ElementsIntoSet
import dagger.multibindings.IntoSet
import okhttp3.Cache
import okhttp3.Interceptor
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
    @Reusable
    fun provideSharedPreferences(app: Application): SharedPreferences =
        app.getSharedPreferences(SHARED_PREF_FILE_NAME, MODE_PRIVATE)

    @JvmStatic
    @Provides
    @Reusable
    fun provideMoshi(factories: Set<@JvmSuppressWildcards JsonAdapter.Factory>): Moshi = Moshi.Builder()
        .add(InstantJsonAdapter())
        .add(LocalDateTimeJsonAdapter())
        .apply { factories.forEach { add(it) } }
        .build()

    @JvmStatic
    @Provides
    @Reusable
    fun provideJsonAdapterFactorySet(): Set<@JvmSuppressWildcards JsonAdapter.Factory> = mutableSetOf()

    @JvmStatic
    @Provides
    @Singleton
    fun provideOkHttpClient(app: Application, interceptors: Set<@JvmSuppressWildcards Interceptor>): OkHttpClient =
        OkHttpClient.Builder()
            .cache(Cache(File(app.cacheDir, "http"), DISK_CACHE_SIZE.toLong()))
            .apply { interceptors.forEach { addInterceptor(it) } }
            .build()

    @JvmStatic
    @Provides
    @Reusable
    @ElementsIntoSet
    fun provideMainInterceptors(app: Application): Set<@JvmSuppressWildcards Interceptor> = setOf(
        UserAgentInterceptor(),
        ChuckInterceptor(app).showNotification(true)
    )

    @JvmStatic
    @SavedTrails
    @Provides
    @Reusable
    fun provideTrailFavorites(prefs: SharedPreferences): Preference<Set<String>> =
        prefs.stringSetPreference("trail-favorites")

    @JvmStatic
    @Provides
    @NotificationTrails
    @Reusable
    fun provideNotificationList(prefs: SharedPreferences): Preference<Set<String>> =
        prefs.stringSetPreference("trail-notifications")
}

@Qualifier annotation class SavedTrails
@Qualifier annotation class NotificationTrails
