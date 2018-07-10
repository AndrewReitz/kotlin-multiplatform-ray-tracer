package cash.andrew.mntrailconditions.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.ui.trails.TrailViewModel
import com.f2prateek.rx.preferences2.Preference
import kotlinx.android.synthetic.main.trail_item_bottom_bar.view.*

/**
 * View that contains actions for trails such as favoring and push notifications.
 */
class TrailItemBottomBar(
        context: Context, attrs: AttributeSet?
) : ConstraintLayout(context, attrs) {

    init {
        inflate(context, R.layout.trail_item_bottom_bar, this)
    }

    fun bind(trail: TrailViewModel, favoriteTrailsPref: Preference<Set<String>>) {
        trail_favorite.apply {
            val favoriteTrails = favoriteTrailsPref.get()
            val contains = favoriteTrails.contains(trail.name)
            setImageResource(if (contains) R.drawable.ic_favorite_selected else R.drawable.ic_favorite_unselected)

            setOnClickListener(favoriteClickListener(trail, favoriteTrailsPref))
        }
    }

    private fun favoriteClickListener(
            trail: TrailViewModel,
            favoriteTrailsPref: Preference<Set<String>>
    ): (view: View) -> Unit = { _ ->
        val favoriteTrails = favoriteTrailsPref.get()
        val contains = favoriteTrails.contains(trail.name)
        trail_favorite.setImageResource(
                if (!contains) R.drawable.ic_favorite_selected else R.drawable.ic_favorite_unselected)
        val updatedTrails = favoriteTrails.toMutableSet().apply {
            if (contains) remove(trail.name) else add(trail.name)
        }
        favoriteTrailsPref.set(updatedTrails)
    }
}
