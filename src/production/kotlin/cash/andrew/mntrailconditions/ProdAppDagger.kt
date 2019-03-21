package cash.andrew.mntrailconditions

import android.app.Application
import cash.andrew.mntrailconditions.data.ProdDataModule
import cash.andrew.mntrailconditions.ui.ActivityHierarchyServer
import cash.andrew.mntrailconditions.ui.UiModule
import cash.andrew.mntrailconditions.util.CrashlyticsTree
import com.crashlytics.android.Crashlytics
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import javax.inject.Singleton

@Singleton
@Component(modules = [MnTrailConditionsModule::class, ProdMnTrailConditionsModule::class])
interface AppComponent: BaseComponent {
    val activityHierarchyServer: ActivityHierarchyServer

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
    fun provideMnTrailConditionsInitializer(application: Application): MnTrailConditionsInitializer = {
        Fabric.with(application, Crashlytics())
        Timber.plant(CrashlyticsTree())
    }
}
