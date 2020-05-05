package cash.andrew.mntrailconditions.ui.trails

import cash.andrew.mntrailconditions.ui.favorites.FavoriteTrailListFragment
import dagger.Module
import dagger.Subcomponent

@Subcomponent(modules = [TrailModule::class])
interface TrailsComponent {
    fun inject(view: TrailListView)
    fun inject(fragment: FavoriteTrailListFragment)
}

@Module
object TrailModule
