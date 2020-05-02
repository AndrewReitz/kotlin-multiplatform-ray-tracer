package cash.andrew.mntrailconditions.ui.trails

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import cash.andrew.kotlin.common.retry
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.data.api.TrailConditionsService
import cash.andrew.mntrailconditions.data.model.TrailData
import cash.andrew.mntrailconditions.data.preference.Preference
import cash.andrew.mntrailconditions.databinding.TrailListViewBinding
import cash.andrew.mntrailconditions.util.component
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

class TrailListView(
        context: Context,
        attrs: AttributeSet
) : LinearLayout(context, attrs), CoroutineScope by MainScope() {

    @Inject lateinit var trailConditionsService: TrailConditionsService
    @Inject lateinit var trailListAdapter: TrailListAdapter

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
            try {
                val trails: List<TrailData> = retry {
                    withTimeout(5000) { trailConditionsService.trailRegions() }
                }

                val filtered = if (favoriteTrailsPref == null) trails
                else trails.filter { trail -> trail.name in requireNotNull(favoriteTrailsPref).get() }

                val sortedViewModels = filtered.map { it.toViewModel() }.sortedBy { it.name }

                launch(Dispatchers.Main) {
                    if (favoriteTrailsPref != null && sortedViewModels.isEmpty()) {
                        animator.displayedChildId = R.id.trail_list_no_favorites_text
                    } else {
                        trailListAdapter.trails = sortedViewModels
                        animator.displayedChildId = R.id.trail_list_recycler_view
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error loading trails")
                launch(Dispatchers.Main) {
                    animator.displayedChildId = R.id.trail_list_error
                }
            }
        }

        refreshLayout.isRefreshing = false
    }
}
