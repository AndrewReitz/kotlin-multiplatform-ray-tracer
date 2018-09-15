package cash.andrew.mntrailconditions.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.ui.trails.TrailViewModel
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
    }

    fun bind(
            trail: TrailViewModel,
            favoriteTrailsPref: Preference<Set<String>>,
            notificationsPref: Preference<Set<String>>,
            firebaseMessaging: FirebaseMessaging
    ) {
        trail_favorite.apply {
            isEnabled = false
            val favoriteTrails = favoriteTrailsPref.get()
            isChecked = favoriteTrails.contains(trail.name)
            setOnCheckedChangeListener(favoriteClickListener(trail, favoriteTrailsPref))
            isEnabled = true
        }

        traiL_notification.apply {
            isEnabled = false
            val notifications = notificationsPref.get()
            isChecked = notifications.contains(trail.name)
            setOnCheckedChangeListener(notificationListener(trail, notificationsPref, firebaseMessaging))
            isEnabled = true
        }
    }

    private fun favoriteClickListener(
            trail: TrailViewModel,
            favoriteTrailsPref: Preference<Set<String>>
    ): (View, Boolean) -> Unit = { _, _ ->
        val favoriteTrails = favoriteTrailsPref.get()
        val contains = favoriteTrails.contains(trail.name)

        val updatedTrails = favoriteTrails.toMutableSet().apply {
            if (contains) remove(trail.name) else add(trail.name)
        }

        favoriteTrailsPref.set(updatedTrails)
    }

    private fun notificationListener(
            trail: TrailViewModel,
            notificationsPref: Preference<Set<String>>,
            firebaseMessaging: FirebaseMessaging
    ): (View, Boolean) -> Unit = { _, _ ->
        val notifications = notificationsPref.get()
        val contains = notifications.contains(trail.name)

        val updated = notifications.toMutableSet().apply {
            if (contains) remove(trail.name) else add(trail.name)
        }

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
