package cash.andrew.mntrailconditions.ui.trails

import dagger.Module
import dagger.Subcomponent

@Subcomponent(modules = [TrailModule::class])
interface TrailsComponent {
    fun inject(view: TrailListView)
}

@Module
object TrailModule
