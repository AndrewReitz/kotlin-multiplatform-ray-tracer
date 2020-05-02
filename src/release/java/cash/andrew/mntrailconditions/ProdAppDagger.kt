package cash.andrew.mntrailconditions

import android.app.Application
import cash.andrew.mntrailconditions.data.ProdDataModule
import cash.andrew.mntrailconditions.ui.ActivityHierarchyServer
import cash.andrew.mntrailconditions.util.CrashlyticsTree
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import timber.log.Timber
import javax.inject.Singleton

@Singleton
@Component(modules = [MnTrailConditionsModule::class, ProdMnTrailConditionsModule::class])
interface AppComponent: BaseComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

    companion object {
        fun builder(): Builder = DaggerAppComponent.builder()
    }
}

@Module(includes = [ProdDataModule::class])
object ProdMnTrailConditionsModule {
    @JvmStatic
    @Provides
    fun provideMnTrailConditionsInitializer(crashlytics: FirebaseCrashlytics): MnTrailConditionsInitializer = {
        Timber.plant(CrashlyticsTree(crashlytics))
    }
}
