package cash.andrew.mntrailconditions

import android.app.Application
import cash.andrew.mntrailconditions.data.DebugDataModule
import cash.andrew.mntrailconditions.ui.DebugUiModule
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.facebook.stetho.Stetho
import com.facebook.stetho.timber.StethoTree
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import io.fabric.sdk.android.Fabric
import timber.log.Timber

@Module(includes = [DebugUiModule::class, DebugDataModule::class])
object DebugMnTrailConditionsModule {
    @JvmStatic
    @Provides
    fun provideMnTrailConditionsInitializer(
            app: Application,
            firebaseMessaging: FirebaseMessaging
    ): MnTrailConditionsInitializer = { context ->
        Stetho.initializeWithDefaults(context)

        Timber.plant(Timber.DebugTree())
        Timber.plant(StethoTree())

        // Set up Crashlytics, disabled for debug builds
        val crashlyticsKit = Crashlytics.Builder()
                .core(CrashlyticsCore.Builder().disabled(true).build())
                .build()

        // Initialize Fabric with the debug-disabled crashlytics.
        Fabric.with(app, crashlyticsKit)

        // allows updating test in firestore to get push notifications
        // and ensure everything is working smoothly.
        firebaseMessaging.subscribeToTopic("test")
    }
}
