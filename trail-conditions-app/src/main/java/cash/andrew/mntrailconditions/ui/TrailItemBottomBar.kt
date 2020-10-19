package cash.andrew.mntrailconditions.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.data.preference.Preference
import cash.andrew.mntrailconditions.databinding.TrailItemBottomBarBinding
import cash.andrew.mntrailconditions.ui.trails.TrailViewModel
import cash.andrew.mntrailconditions.util.IntentManager
import cash.andrew.mntrailconditions.util.setToolTipTextCompat
import cash.andrew.mntrailconditions.util.subscribeToTopicV2
import cash.andrew.mntrailconditions.util.unsubscribeFromTopicV2
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import timber.log.Timber

/**
 * View that contains actions for trails such as favoring and push notifications.
 */
class TrailItemBottomBar(
  context: Context, attrs: AttributeSet?
) : ConstraintLayout(context, attrs) {

  private val binding: TrailItemBottomBarBinding

  init {
    inflate(context, R.layout.trail_item_bottom_bar, this)
    binding = TrailItemBottomBarBinding.bind(this)

    binding.trailFavorite.setToolTipTextCompat(R.string.favorite)
    binding.trailNotification.setToolTipTextCompat(R.string.notification)
  }

  fun bind(
    trail: TrailViewModel,
    favoriteTrailsPref: Preference<Set<String>>,
    notificationsPref: Preference<Set<String>>,
    firebaseMessaging: FirebaseMessaging,
    firebaseAnalytics: FirebaseAnalytics,
    intentManager: IntentManager
  ) {

    binding.trailFavoriteContainer.setOnClickListener {
      binding.trailFavorite.isChecked = !binding.trailFavorite.isChecked
    }

    binding.trailFavorite.apply {
      setOnCheckedChangeListener(null)
      isEnabled = false
      isChecked = favoriteTrailsPref.get().contains(trail.name)
      setOnCheckedChangeListener(
        favoriteClickListener(trail, favoriteTrailsPref, firebaseAnalytics)
      )
      isEnabled = true
    }

    binding.trailNotificationContainer.setOnClickListener {
      binding.trailNotification.isChecked = !binding.trailNotification.isChecked
    }

    binding.trailNotification.apply {
      setOnCheckedChangeListener(null)
      isEnabled = false
      isChecked = notificationsPref.get().contains(trail.name)
      setOnCheckedChangeListener(
        notificationListener(trail, notificationsPref, firebaseMessaging, firebaseAnalytics)
      )
      isEnabled = true
    }

    binding.mountainProject.bindSocialMediaButton(trail.toMountainProjectSocialMediaViewModel(), intentManager)
    binding.facebook.bindSocialMediaButton(trail.toFacebookSocialMediaViewModel(), intentManager)
    binding.twitter.bindSocialMediaButton(trail.toTwitterSocialMediaViewModel(), intentManager)
  }

  private fun TrailViewModel.toMountainProjectSocialMediaViewModel() =
    if (mountainProjectUrl == null) SocialMediaButtonViewModel()
    else
      SocialMediaButtonViewModel(
        url = Uri.parse(mountainProjectUrl),
        contentDescription = "$name mountain project webpage"
      )

  private fun TrailViewModel.toFacebookSocialMediaViewModel() =
    if (facebookUrl == null) SocialMediaButtonViewModel()
    else SocialMediaButtonViewModel(
      url = Uri.parse(facebookUrl),
      contentDescription = "$name facebook page"
    )

  private fun TrailViewModel.toTwitterSocialMediaViewModel() =
    if (twitterUrl == null) SocialMediaButtonViewModel()
    else SocialMediaButtonViewModel(
      url = Uri.parse(twitterUrl),
      contentDescription = "$name twitter page"
    )

  private data class SocialMediaButtonViewModel(
    val url: Uri? = null,
    val contentDescription: String = ""
  )

  private fun ImageButton.bindSocialMediaButton(
    buttonViewModel: SocialMediaButtonViewModel,
    intentManager: IntentManager
  ) {
    if (buttonViewModel.url == null) {
      visibility = View.GONE
      return
    }
    visibility = View.VISIBLE
    contentDescription = buttonViewModel.contentDescription
    setOnClickListener {
      val browserIntent = Intent(Intent.ACTION_VIEW, buttonViewModel.url)
      if (!intentManager.maybeStartChooser(browserIntent)) {
        Timber.w("maybeStartChooser: Unable to open url=${buttonViewModel.url}")
      }
    }
  }

  private fun favoriteClickListener(
    trail: TrailViewModel,
    favoriteTrailsPref: Preference<Set<String>>,
    firebaseAnalytics: FirebaseAnalytics
  ): (View, Boolean) -> Unit = { _, enabled ->
    Timber.d("favoriteClickListener() enabled = [%s]", enabled)
    Timber.d("trailName = [%s]", trail.name)

    val bundle = Bundle().apply {
      putString("favorite_name", trail.name)
    }

    val favorite = if (enabled) "favorite_endabled" else "favorite_disabled"
    firebaseAnalytics.logEvent(favorite, bundle)

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
    firebaseMessaging: FirebaseMessaging,
    firebaseAnalytics: FirebaseAnalytics
  ): (View, Boolean) -> Unit = { _, enabled ->
    Timber.d("notificationListener() enabled = [%s]", enabled)
    Timber.d("trailName = [%s]", trail.name)

    val bundle = Bundle().apply {
      putString("notification_name", trail.name)
    }

    val event = if (enabled) "notification_enabled" else "notification_disabled"
    firebaseAnalytics.logEvent(event, bundle)

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
      firebaseMessaging.unsubscribeFromTopicV2(trailName)
        .addOnSuccessListener { Timber.i("Success un-subscribing to $trailName") }
        .addOnFailureListener { Timber.e(it, "Error removing notifications for $trailName") }
    }

    subscribe.forEach { trailName ->
      firebaseMessaging.subscribeToTopicV2(trailName)
        .addOnSuccessListener { Timber.i("Success subscribing to $trailName") }
        .addOnFailureListener { Timber.e(it, "Error subscribing to notifications for $trailName") }
    }
  }
}
