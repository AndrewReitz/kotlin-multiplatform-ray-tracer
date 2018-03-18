package cash.andrew.mntrailconditions.data

import cash.andrew.mntrailconditions.data.api.DebugApiModule

import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier annotation class ApiEndpoint
@Qualifier annotation class AnimationSpeed
@Qualifier annotation class CaptureIntents
@Qualifier annotation class NetworkDelay
@Qualifier annotation class NetworkFailurePercent
@Qualifier annotation class NetworkVariancePercent
@Qualifier annotation class SeenDebugDrawer

@Module(includes = [DebugApiModule::class])
object DebugDataModule {

    private const val DEFAULT_ANIMATION_SPEED = 1 // 1x (normal) speed.
    private const val DEFAULT_SEEN_DEBUG_DRAWER = false // Show debug drawer first time.
    private const val DEFAULT_CAPTURE_INTENTS = true // Capture external intents.

    @JvmStatic
    @Provides
    @Singleton
    @ApiEndpoint
    fun provideEndpointPreference(preferences: RxSharedPreferences): Preference<String> =
            preferences.getString("debug_endpoint", ApiEndpoints.PRODUCTION.url!!)

    @JvmStatic
    @Provides
    @Singleton
    @NetworkDelay
    fun provideNetworkDelay(preferences: RxSharedPreferences): Preference<Long> =
            preferences.getLong("debug_network_delay", 2000L)

    @JvmStatic
    @Provides
    @Singleton
    @NetworkFailurePercent
    fun provideNetworkFailurePercent(preferences: RxSharedPreferences): Preference<Int> =
            preferences.getInteger("debug_network_failure_percent", 3)

    @JvmStatic
    @Provides
    @Singleton
    @NetworkVariancePercent
    fun provideNetworkVariancePercent(preferences: RxSharedPreferences): Preference<Int> =
            preferences.getInteger("debug_network_variance_percent", 40)

    @JvmStatic
    @Provides
    @Singleton
    @CaptureIntents
    fun provideCaptureIntentsPreference(preferences: RxSharedPreferences): Preference<Boolean> =
            preferences.getBoolean("debug_capture_intents", DEFAULT_CAPTURE_INTENTS)

    @JvmStatic
    @Provides
    @Singleton
    @AnimationSpeed
    fun provideAnimationSpeed(preferences: RxSharedPreferences): Preference<Int> =
            preferences.getInteger("debug_animation_speed", DEFAULT_ANIMATION_SPEED)

    @JvmStatic
    @Provides
    @Singleton
    @SeenDebugDrawer
    fun provideSeenDebugDrawer(preferences: RxSharedPreferences): Preference<Boolean> =
            preferences.getBoolean("debug_seen_debug_drawer", DEFAULT_SEEN_DEBUG_DRAWER)
}
