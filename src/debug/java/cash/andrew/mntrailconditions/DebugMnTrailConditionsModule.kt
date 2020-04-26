package cash.andrew.mntrailconditions

import android.app.Application
import cash.andrew.mntrailconditions.data.DebugDataModule
import com.facebook.stetho.Stetho
import com.facebook.stetho.timber.StethoTree
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import timber.log.Timber
import android.os.StrictMode

@Module(includes = [DebugDataModule::class])
object DebugMnTrailConditionsModule {
    @JvmStatic
    @Provides
    fun provideMnTrailConditionsInitializer(
            firebaseMessaging: FirebaseMessaging
    ): MnTrailConditionsInitializer = { context ->
        Stetho.initializeWithDefaults(context)

        Timber.plant(Timber.DebugTree())
        Timber.plant(StethoTree())

        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build())

        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build())

        // allows updating test in firestore to get push notifications
        // and ensure everything is working smoothly.
        firebaseMessaging.subscribeToTopic("test")
    }
}
