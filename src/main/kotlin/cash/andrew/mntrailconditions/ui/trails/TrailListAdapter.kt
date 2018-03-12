package cash.andrew.mntrailconditions.ui.trails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cash.andrew.mntrailconditions.R
import com.andrewreitz.velcro.BindableRecyclerAdapter
import javax.inject.Inject

class TrailListAdapter @Inject constructor() : BindableRecyclerAdapter<TrailViewModel>() {
    var trails: List<TrailViewModel> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun newView(layoutInflater: LayoutInflater, position: Int, viewGroup: ViewGroup) =
            layoutInflater.inflate(R.layout.trail_list_item_view, viewGroup, false)!!

    override fun getItem(position: Int) = trails[position]

    override fun bindView(trail: TrailViewModel, view: View, position: Int) {
        (view as TrailListItemView).bind(trail)
    }

    override fun getItemCount() = trails.size
}
