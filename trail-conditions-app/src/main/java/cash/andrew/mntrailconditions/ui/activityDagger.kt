package cash.andrew.mntrailconditions.ui

import android.app.Activity
import cash.andrew.mntrailconditions.ui.trails.TrailsComponent
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import io.noties.markwon.Markwon
import javax.inject.Scope

@Scope
annotation class ActivityScope

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    val trailsComponent: TrailsComponent

    fun inject(activity: MainActivity)
    fun inject(activity: MarkdownActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance fun activity(activity: Activity): Builder
        fun build(): ActivityComponent
    }
}

@Module
object ActivityModule {
    @ActivityScope
    @Provides
    @JvmStatic
    fun provideMarkwon(activity: Activity): Markwon = Markwon.create(activity)
}
