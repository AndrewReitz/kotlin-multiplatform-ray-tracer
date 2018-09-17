package cash.andrew.mntrailconditions.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.ui.trails.TrailViewModel
import cash.andrew.mntrailconditions.util.setToolTipTextCompat
import cash.andrew.mntrailconditions.util.toTopicName
import com.f2prateek.rx.preferences2.Preference
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.trail_item_bottom_bar.view.*
import timber.log.Timber

/**
 * View that contains actions for trails such as favoring and push notifications.
 */
class TrailItemBottomBar(
        context: Context, attrs: AttributeSet?
) : ConstraintLayout(context, attrs) {

    init {
        inflate(context, R.layout.trail_item_bottom_bar, this)

        trail_favorite.setToolTipTextCompat(R.string.favorite)
        traiL_notification.setToolTipTextCompat(R.string.notification)
    }

    fun bind(
            trail: TrailViewModel,
            favoriteTrailsPref: Preference<Set<String>>,
            notificationsPref: Preference<Set<String>>,
            firebaseMessaging: FirebaseMessaging
    ) {
        trail_favorite.apply {
            setOnCheckedChangeListener(null)
            isEnabled = false
            isChecked = favoriteTrailsPref.get().contains(trail.name)
            setOnCheckedChangeListener(favoriteClickListener(trail, favoriteTrailsPref))
            isEnabled = true
        }

        traiL_notification.apply {
            setOnCheckedChangeListener(null)
            isEnabled = false
            isChecked = notificationsPref.get().contains(trail.name)
            setOnCheckedChangeListener(notificationListener(trail, notificationsPref, firebaseMessaging))
            isEnabled = true
        }
    }

    private fun favoriteClickListener(
            trail: TrailViewModel,
            favoriteTrailsPref: Preference<Set<String>>
    ): (View, Boolean) -> Unit = { _, enabled ->
        Timber.d("favoriteClickListener() enabled = [%s]", enabled)
        Timber.d("trailName = [%s]", trail.name)

        val favoriteTrails = favoriteTrailsPref.get()
        Timber.d("favoriteTrails = [%s]", favoriteTrails)

        val updatedTrails = favoriteTrails.toMutableSet().apply {
            if (enabled) add(trail.name) else remove(trail.name)
        }
        Timber.d("after update favoriteTrails = [%s]", updatedTrails)

        favoriteTrailsPref.set(updatedTrails)
    }

    private fun notificationListener(
            trail: TrailViewModel,
            notificationsPref: Preference<Set<String>>,
            firebaseMessaging: FirebaseMessaging
    ): (View, Boolean) -> Unit = { _, enabled ->
        Timber.d("notificationListener() enabled = [%s]", enabled)
        Timber.d("trailName = [%s]", trail.name)

        val notifications = notificationsPref.get()
        Timber.d("notifications = [%s]", notifications)

        val updated = notifications.toMutableSet().apply {
            if (enabled) add(trail.name) else remove(trail.name)
        }
        Timber.d("after update notifications = [%s]", updated)

        notificationsPref.set(updated)

        val unSubscribe = notifications - updated
        val subscribe = updated - notifications

        unSubscribe.forEach { trailName ->
            firebaseMessaging.unsubscribeFromTopic(trailName.toTopicName())
                    .addOnSuccessListener { Timber.i("Success un-subscribing to ${trailName.toTopicName()}") }
                    .addOnFailureListener { Timber.e(it, "Error removing notifications for ${trailName.toTopicName()}") }
        }

        subscribe.forEach { trailName ->
            firebaseMessaging.subscribeToTopic(trailName.toTopicName())
                    .addOnSuccessListener { Timber.i("Success subscribing to ${trailName.toTopicName()}") }
                    .addOnFailureListener { Timber.e(it, "Error subscribing to notifications for ${trailName.toTopicName()}") }
        }
    }
}
