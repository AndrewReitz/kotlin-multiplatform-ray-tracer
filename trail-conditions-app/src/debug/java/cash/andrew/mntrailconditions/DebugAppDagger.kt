package cash.andrew.mntrailconditions

import android.app.Application
import cash.andrew.mntrailconditions.ui.debug.DebugView
import dagger.BindsInstance
import dagger.Component
import dagger.Dagger
import javax.inject.Singleton

@Singleton
@Component(modules = [MnTrailConditionsModule::class, DebugMnTrailConditionsModule::class])
interface AppComponent: BaseComponent {
    fun inject(view: DebugView)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

    companion object {
        fun builder(): Builder =  Dagger.builder(Builder::class.java)
    }
}
