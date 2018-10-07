package cash.andrew.mntrailconditions.ui.trails

import android.content.Context
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.util.statusToResource
import com.f2prateek.rx.preferences2.Preference
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.trail_list_item_view.view.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.temporal.ChronoUnit

class TrailListItemView(context: Context, attrs: AttributeSet) : CardView(context, attrs) {

    private inline val title get() = trail_list_item_title
    private inline val conditionsImage get() = trail_list_item_conditions_image
    private inline val conditionsText get() = trail_list_item_condition_text
    private inline val detailsText get() = trail_list_item_details
    private inline val lastUpdatedText get() = trail_list_last_updated_time
    private inline val bottomBar get() = trail_list_bottom_bar

    fun bind(
            trail: TrailViewModel,
            favoriteTrailsPref: Preference<Set<String>>,
            notificationPref: Preference<Set<String>>,
            firebaseMessaging: FirebaseMessaging,
            firebaseAnalytics: FirebaseAnalytics
    ) {
        with(trail) {
            val now = LocalDateTime.now()
            val updated = LocalDateTime.ofInstant(updatedAt, ZoneId.systemDefault())
            val days = ChronoUnit.DAYS.between(updated, now).toInt()
            val hours = ChronoUnit.HOURS.between(updated, now).toInt()
            val minutes = ChronoUnit.MINUTES.between(updated, now).toInt()
            val lastUpdated = when (days) {
                0 -> when (hours) {
                    0 -> resources.getQuantityString(R.plurals.last_updated_minutes, minutes, minutes)
                    else -> resources.getQuantityString(R.plurals.last_updated_hours, hours, hours)
                }
                else -> resources.getQuantityString(R.plurals.last_updated_days, days, days)
            }

            title.text = name
            conditionsText.text = status
            detailsText.text = description
            lastUpdatedText.text = lastUpdated
            conditionsImage.setImageDrawable(context.getDrawable(statusToResource(status)))
        }

        bottomBar.bind(
                trail,
                favoriteTrailsPref,
                notificationPref,
                firebaseMessaging,
                firebaseAnalytics
        )
    }
}
