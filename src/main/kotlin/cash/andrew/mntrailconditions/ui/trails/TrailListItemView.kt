package cash.andrew.mntrailconditions.ui.trails

import android.content.Context
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.util.statusToResource
import kotlinx.android.synthetic.main.trail_list_item_view.view.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.temporal.ChronoUnit

class TrailListItemView(context: Context, attrs: AttributeSet) : CardView(context, attrs) {

    private val title get() = trail_list_item_title
    private val conditionsImage get() = trail_list_item_conditions_image
    private val conditionsText get() = trail_list_item_condition_text
    private val detailsText get() = trail_list_item_details
    private val lastUpdatedText get() = trail_list_last_updated_time

    fun bind(trail: TrailViewModel) {
        with(trail) {
            val now = LocalDateTime.now()
            val updated = LocalDateTime.ofInstant(updatedAt, ZoneId.systemDefault())
            val days = ChronoUnit.DAYS.between(updated, now).toInt()
            val hours = ChronoUnit.HOURS.between(updated, now).toInt()
            val lastUpdated = when (days) {
                0 -> resources.getQuantityString(R.plurals.last_updated_hours, hours, hours)
                else -> resources.getQuantityString(R.plurals.last_updated_days, days, days)
            }

            title.text = name
            conditionsText.text = status
            detailsText.text = description
            lastUpdatedText.text = lastUpdated
            conditionsImage.setImageDrawable(context.getDrawable(statusToResource(status)))
        }
    }
}
