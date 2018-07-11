package cash.andrew.mntrailconditions

import cash.andrew.mntrailconditions.data.DataModule
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DataModule::class])
object MnTrailConditionsModule {
    @JvmStatic
    @Provides
    @Singleton
    fun provideFirebaseMessaging() = FirebaseMessaging.getInstance()
}
