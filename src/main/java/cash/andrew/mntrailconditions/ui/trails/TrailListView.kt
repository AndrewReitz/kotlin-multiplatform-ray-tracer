package cash.andrew.mntrailconditions.ui.trails

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.data.api.TrailConditionsService
import cash.andrew.mntrailconditions.databinding.TrailListViewBinding
import cash.andrew.mntrailconditions.util.activityComponent
import cash.andrew.mntrailconditions.util.data
import cash.andrew.mntrailconditions.util.isNotSuccessful
import cash.andrew.mntrailconditions.util.isSuccessful
import cash.andrew.mntrailconditions.util.observeOnMainThread
import cash.andrew.mntrailconditions.util.retryOnUnsuccessfulResult
import cash.andrew.mntrailconditions.util.retryWithTimeout
import com.f2prateek.rx.preferences2.Preference
import com.uber.autodispose.android.scope
import com.uber.autodispose.autoDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class TrailListView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    @Inject lateinit var trailConditionsService: TrailConditionsService
    @Inject lateinit var trailListAdapter: TrailListAdapter

    private val recyclerView get() = binding.trailListRecyclerView
    private val refreshLayout get() = binding.trailListContent
    private val animator get() = binding.trailListAnimator

    var favoriteTrailsPref: Preference<Set<String>>? = null

    private lateinit var binding: TrailListViewBinding

    init {
        context.activityComponent.trailsComponent.inject(this)
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

        val trailData = trailConditionsService.trailDataV3()
                .retryOnUnsuccessfulResult(3)
                .retryWithTimeout()
                .doOnSuccess { result ->
                    if (result.isError) {
                        Timber.e(result.error(), "Failed to get trail data from v3 api")
                    } else {
                        val response = result.response()
                        Timber.e("Failed to get trail data from v3 api. Server returned %d", response?.code())
                    }
                }
                .toObservable()
                .replay(1)
                .autoConnect(-1)
                .singleOrError()

        val trailsV3 = trailData.filter { result -> result.isSuccessful }
                .map { result -> result.data }
                .doOnSuccess { Timber.v("trail data list: %s", it) }
                .toObservable()
                .flatMapIterable { it }
                .map { data -> data.toViewModel() }
                .toList()
                .filter { it.isNotEmpty() }
                .map { trails -> trails.sortedBy { it.name } }

        val trailRegions = trailData.filter { it.isNotSuccessful }
                .flatMap {
                    trailConditionsService.trailRegionsV2()
                            .retryOnUnsuccessfulResult(5)
                            .retryWithTimeout(timeout = 5) // can be very slow to update
                            .toMaybe()
                }
                .toObservable()
                .replay(1)
                .autoConnect(-1)
                .singleOrError()

        val trailsV2 = trailRegions.filter { result -> result.isSuccessful }
                .map { result -> result.data }
                .toObservable()
                .flatMapIterable { it }
                // todo make this an option in settings
//                .filter { trail -> trail.lastUpdated.isAfter(THREE_MONTHS_BEFORE_TODAY) }
                .map { trail -> trail.toViewModel() }
                .toList()
                .filter { it.isNotEmpty() }
                .map { trails -> trails.sortedBy { it.name } }

        trailsV3.concatWith(trailsV2)
                .toObservable()
                .map { trails ->
                    if (favoriteTrailsPref == null) trails
                    else trails.filter { trail -> trail.name in favoriteTrailsPref!!.get() }
                }
                .subscribeOn(Schedulers.io())
                .observeOnMainThread()
                .autoDisposable(scope())
                .subscribe(
                        { trails ->
                            if (favoriteTrailsPref != null && trails.isEmpty()) {
                                animator.displayedChildId = R.id.trail_list_no_favorites_text
                            } else {
                                trailListAdapter.trails = trails
                                animator.displayedChildId = R.id.trail_list_recycler_view
                            }
                        },
                        { exception -> Timber.e(exception, "Something went wrong") }
                )

        trailRegions.filter { result -> result.isNotSuccessful }
                .observeOnMainThread()
                .doOnSuccess { result ->
                    if (result.isError) {
                        Timber.e(result.error(), "Failed to get trail regions")
                    } else {
                        val response = result.response()
                        Timber.e("Failed to get trail regions. Server returned %d", response?.code())
                    }
                }
                .autoDisposable(scope())
                .subscribe(
                        { animator.displayedChildId = R.id.trail_list_error },
                        { exception -> Timber.e(exception, "Something went wrong showing an error") }
                )

        refreshLayout.isRefreshing = false
    }
}
