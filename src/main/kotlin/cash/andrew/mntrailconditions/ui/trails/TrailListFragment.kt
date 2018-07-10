package cash.andrew.mntrailconditions.ui.trails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cash.andrew.mntrailconditions.R

class TrailListFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.trail_list_view, container, false)
            .let { it as TrailListView }
}
