package cash.andrew.mntrailconditions

import android.app.Application
import cash.andrew.mntrailconditions.data.DataModule
import cash.andrew.mntrailconditions.data.LumberYard
import cash.andrew.mntrailconditions.ui.ActivityComponent
import cash.andrew.mntrailconditions.ui.ActivityHierarchyServer
import cash.andrew.mntrailconditions.ui.debug.DebugView
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Singleton
@Component(modules = [MnTrailConditionsModule::class, DebugMnTrailConditionsModule::class])
interface AppComponent {
    val activityHierarchyServer: ActivityHierarchyServer
    val lumberYard: LumberYard
    val appInitializer: MnTrailConditionsInitializer

    val activityComponentBuilder: ActivityComponent.Builder

    fun inject(view: DebugView)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}

@Module(includes = [DataModule::class])
object MnTrailConditionsModule
