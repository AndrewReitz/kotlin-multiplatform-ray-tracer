package cash.andrew.mntrailconditions

import cash.andrew.mntrailconditions.data.DebugDataModule
import cash.andrew.mntrailconditions.ui.DebugUiModule
import com.facebook.stetho.Stetho
import com.facebook.stetho.timber.StethoTree
import dagger.Module
import dagger.Provides
import timber.log.Timber

@Module(includes = [DebugUiModule::class, DebugDataModule::class])
object DebugMnTrailConditionsModule {
    @JvmStatic
    @Provides
    fun provideMnTrailConditionsInitializer(): MnTrailConditionsInitializer = { context ->
        Stetho.initializeWithDefaults(context)

        Timber.plant(Timber.DebugTree())
        Timber.plant(StethoTree())
    }
}
