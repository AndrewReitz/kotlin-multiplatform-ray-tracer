package cash.andrew.mntrailconditions.ui.trails

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import cash.andrew.kotlin.common.doOnFailure
import cash.andrew.kotlin.common.doOnSuccess
import cash.andrew.kotlin.common.flatMapFailure
import cash.andrew.kotlin.common.map
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.data.preference.Preference
import cash.andrew.mntrailconditions.databinding.TrailListViewBinding
import cash.andrew.mntrailconditions.util.component
import kotlinx.coroutines.*
import timber.log.Timber
import trail.networking.HtmlMorcTrailRepository
import trail.networking.MorcTrailRepository
import javax.inject.Inject

class TrailListView(
  context: Context,
  attrs: AttributeSet
) : LinearLayout(context, attrs), CoroutineScope by MainScope() {

  @Inject lateinit var htmlMorcTrailRepository: HtmlMorcTrailRepository
  @Inject lateinit var trailListAdapter: TrailListAdapter
  @Inject lateinit var morcTrailRepository: MorcTrailRepository

  private val recyclerView get() = binding.trailListRecyclerView
  private val refreshLayout get() = binding.trailListContent
  private val animator get() = binding.trailListAnimator

  var favoriteTrailsPref: Preference<Set<String>>? = null

  private lateinit var binding: TrailListViewBinding

  init {
    component.trailsComponent.inject(this)
  }

  override fun onFinishInflate() {
    super.onFinishInflate()

    binding = TrailListViewBinding.bind(this)

    recyclerView.layoutManager = LinearLayoutManager(context)
    recyclerView.adapter = trailListAdapter

    refreshLayout.setOnRefreshListener {
      Timber.d("onRefresh() called")
      loadData()
    }
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()

    Timber.d("onAttachedToWindow() called")
    loadData()
  }

  override fun onDetachedFromWindow() {
    Timber.d("onDetachedFromWindow() called")
    super.onDetachedFromWindow()
  }

  private fun loadData() {
    Timber.d("loadData() called")

    launch(Dispatchers.IO) {
      morcTrailRepository.getTrails().map { trails ->
        val filtered = if (favoriteTrailsPref == null) trails
        else trails.filter { trail -> trail.trailName in requireNotNull(favoriteTrailsPref).get() }
        filtered.map { it.toViewModel() }.sortedBy { it.name }
      }.flatMapFailure { error ->
        Timber.e(error, "Error loading trails from morc trails")

        htmlMorcTrailRepository.getTrails().map { trails ->
          val filtered = if (favoriteTrailsPref == null) trails
          else trails.filter { trail -> trail.name in requireNotNull(favoriteTrailsPref).get() }
          filtered.map { it.toViewModel() }.sortedBy { it.name }
        }.doOnFailure {
          Timber.e(it, "Error loading trails from heroku")
          launch(Dispatchers.Main) {
            animator.displayedChildId = R.id.trail_list_error
          }
        }
      }.doOnSuccess { viewModels ->
        launch(Dispatchers.Main) {
          if (favoriteTrailsPref != null && viewModels.isEmpty()) {
            animator.displayedChildId = R.id.trail_list_no_favorites_text
          } else {
            trailListAdapter.trails = viewModels
            animator.displayedChildId = R.id.trail_list_recycler_view
          }
        }
      }

      launch(Dispatchers.Main) {
        refreshLayout.isRefreshing = false
      }
    }
  }
}
