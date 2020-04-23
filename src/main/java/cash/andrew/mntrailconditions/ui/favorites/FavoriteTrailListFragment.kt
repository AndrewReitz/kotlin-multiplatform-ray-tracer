package cash.andrew.mntrailconditions.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.data.SavedTrails
import cash.andrew.mntrailconditions.data.preference.Preference
import cash.andrew.mntrailconditions.ui.trails.TrailListView
import cash.andrew.mntrailconditions.util.activityComponent
import javax.inject.Inject

class FavoriteTrailListFragment : Fragment() {

    @Inject
    @field:SavedTrails
    lateinit var favoriteTrailsPref: Preference<Set<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity!!.activityComponent.trailsComponent.inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.trail_list_view, container, false)
            .let { view -> view as TrailListView }
            .also { view -> view.favoriteTrailsPref = favoriteTrailsPref }
}
