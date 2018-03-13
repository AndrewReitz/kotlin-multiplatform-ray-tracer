package cash.andrew.mntrailconditions

import butterknife.ButterKnife
import cash.andrew.mntrailconditions.data.DebugDataModule
import cash.andrew.mntrailconditions.ui.DebugUiModule
import com.facebook.stetho.Stetho
import com.facebook.stetho.timber.StethoTree
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import timber.log.Timber

@Module(includes = [DebugUiModule::class, DebugDataModule::class])
object DebugMnTrailConditionsModule {
    @JvmStatic
    @Provides
    fun provideMnTrailConditionsInitializer(): MnTrailConditionsInitializer = { context ->
        ButterKnife.setDebug(true)

        Stetho.initializeWithDefaults(context)

        Timber.plant(Timber.DebugTree())
        Timber.plant(StethoTree())
    }
}
