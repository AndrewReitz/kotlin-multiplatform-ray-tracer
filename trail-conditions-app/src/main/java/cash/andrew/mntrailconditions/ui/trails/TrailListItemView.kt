package cash.andrew.mntrailconditions.ui.trails

import android.content.Context
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.data.preference.Preference
import cash.andrew.mntrailconditions.databinding.TrailListItemViewBinding
import cash.andrew.mntrailconditions.util.IntentManager
import cash.andrew.mntrailconditions.util.statusToColor
import cash.andrew.mntrailconditions.util.statusToResource
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit

class TrailListItemView(context: Context, attrs: AttributeSet) : CardView(context, attrs) {

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
    firebaseAnalytics: FirebaseAnalytics,
    intentManager: IntentManager
  ) {
    with(trail) {
      val now = LocalDateTime.now()
      val months = ChronoUnit.MONTHS.between(updatedAt, now).toInt()
      val days = ChronoUnit.DAYS.between(updatedAt, now).toInt()
      val hours = ChronoUnit.HOURS.between(updatedAt, now).toInt()
      val minutes = ChronoUnit.MINUTES.between(updatedAt, now).toInt()
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

      val trailStatus = status

      with(binding) {
        title.text = name
        status.text = trailStatus
        status.setTextColor(statusToColor(context, trailStatus))
        details.text = description
        lastUpdatedTime.text = lastUpdated
        statusImage.setImageDrawable(context.getDrawable(statusToResource(trailStatus)))
      }
    }

    binding.bottomBar.bind(
      trail,
      favoriteTrailsPref,
      notificationPref,
      firebaseMessaging,
      firebaseAnalytics,
      intentManager
    )
  }
}
