package cash.andrew.mntrailconditions

import android.app.Application
import cash.andrew.mntrailconditions.data.DataModule
import cash.andrew.mntrailconditions.data.LumberYard
import cash.andrew.mntrailconditions.data.ProdDataModule
import cash.andrew.mntrailconditions.ui.ActivityComponent
import cash.andrew.mntrailconditions.ui.ActivityHierarchyServer
import cash.andrew.mntrailconditions.ui.UiModule
import cash.andrew.mntrailconditions.util.CrashlyticsTree
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import timber.log.Timber
import javax.inject.Singleton

@Singleton
@Component(modules = [MnTrailConditionsModule::class, ProdMnTrailConditionsModule::class])
interface AppComponent {
    val activityHierarchyServer: ActivityHierarchyServer
    val lumberYard: LumberYard
    val appInitializer: MnTrailConditionsInitializer

    val activityComponentBuilder: ActivityComponent.Builder

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}

@Module(includes = [UiModule::class, ProdDataModule::class])
object ProdMnTrailConditionsModule {
    @JvmStatic
    @Provides
    fun provideMnTrailConditionsInitializer(): MnTrailConditionsInitializer = {
        Timber.plant(CrashlyticsTree())
    }
}
