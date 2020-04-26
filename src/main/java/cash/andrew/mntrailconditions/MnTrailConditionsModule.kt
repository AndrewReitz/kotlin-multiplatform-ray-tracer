package cash.andrew.mntrailconditions

import android.app.Application
import cash.andrew.mntrailconditions.data.DataModule
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module(includes = [DataModule::class])
object MnTrailConditionsModule {
    @JvmStatic
    @Provides
    @Reusable
    fun provideFirebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()

    @JvmStatic
    @Provides
    @Reusable
    fun provideFirebaseAnalytics(context: Application) = FirebaseAnalytics.getInstance(context)

    @JvmStatic
    @Provides
    @Reusable
    fun provideFirebaseCrashlytics() = FirebaseCrashlytics.getInstance()
}
