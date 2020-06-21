package cash.andrew.mntrailconditions.ui.trails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.data.NotificationTrails
import cash.andrew.mntrailconditions.data.SavedTrails
import cash.andrew.mntrailconditions.data.preference.Preference
import cash.andrew.mntrailconditions.ui.misc.BindableRecyclerAdapter
import cash.andrew.mntrailconditions.util.IntentManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject

class TrailListAdapter @Inject constructor(
  @SavedTrails private val favoriteTrailsPref: Preference<Set<String>>,
  @NotificationTrails private val notificationPref: Preference<Set<String>>,
  private val firebaseMessaging: FirebaseMessaging,
  private val firebaseAnalytics: FirebaseAnalytics,
  private val intentManager: IntentManager
) : BindableRecyclerAdapter<TrailViewModel>() {
  var trails: List<TrailViewModel> = listOf()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun newView(layoutInflater: LayoutInflater, viewType: Int, parent: ViewGroup) =
    layoutInflater.inflate(R.layout.trail_list_item_view, parent, false)!!

  override fun getItem(position: Int) = trails[position]

  override fun bindView(item: TrailViewModel, view: View, position: Int) {
    (view as TrailListItemView).bind(
      item,
      favoriteTrailsPref,
      notificationPref,
      firebaseMessaging,
      firebaseAnalytics,
      intentManager
    )
  }

  override fun getItemCount() = trails.size
}
