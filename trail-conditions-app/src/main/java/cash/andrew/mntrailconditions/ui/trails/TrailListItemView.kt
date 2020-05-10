package cash.andrew.mntrailconditions.ui.trails

import android.content.Context
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.data.preference.Preference
import cash.andrew.mntrailconditions.databinding.TrailListItemViewBinding
import cash.andrew.mntrailconditions.util.statusToResource
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.temporal.ChronoUnit

class TrailListItemView(context: Context, attrs: AttributeSet) : CardView(context, attrs) {

    private inline val title get() = binding.trailListItemTitle
    private inline val conditionsImage get() = binding.trailListItemConditionsImage
    private inline val conditionsText get() = binding.trailListItemConditionText
    private inline val detailsText get() = binding.trailListItemDetails
    private inline val lastUpdatedText get() = binding.trailListLastUpdatedTime
    private inline val bottomBar get() = binding.trailListBottomBar

    private lateinit var binding: TrailListItemViewBinding

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding = TrailListItemViewBinding.bind(this)
    }

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
            val months = ChronoUnit.MONTHS.between(updated, now).toInt()
            val days = ChronoUnit.DAYS.between(updated, now).toInt()
            val hours = ChronoUnit.HOURS.between(updated, now).toInt()
            val minutes = ChronoUnit.MINUTES.between(updated, now).toInt()
            val lastUpdated = when (months) {
                0 -> when (days) {
                    0 -> when (hours) {
                        0 -> resources.getQuantityString(R.plurals.last_updated_minutes, minutes, minutes)
                        else -> resources.getQuantityString(R.plurals.last_updated_hours, hours, hours)
                    }
                    else -> resources.getQuantityString(R.plurals.last_updated_days, days, days)
                }
                else -> resources.getQuantityString(R.plurals.last_updated_months, months, months)
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
