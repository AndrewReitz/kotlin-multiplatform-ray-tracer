package cash.andrew.mntrailconditions.ui.trails

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.widget.LinearLayout
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.data.Injector
import cash.andrew.mntrailconditions.data.api.TrailConditionsService
import cash.andrew.mntrailconditions.util.*
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.trail_list_view.view.*
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import javax.inject.Inject

class TrailListView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    companion object {
        private val THREE_MONTHS_BEFORE_TODAY = LocalDateTime.now().minusMonths(3)
    }

    @Inject lateinit var trailConditionsService: TrailConditionsService
    @Inject lateinit var trailListAdapter: TrailListAdapter

    private val subscriptions: CompositeDisposable = CompositeDisposable()

    private val recyclerView by lazy { trail_list_recycler_view }
    private val refreshLayout by lazy { trail_list_content }
    private val animator by lazy { trail_list_animator }

    init {
        if (!isInEditMode) {
            Injector.obtain(context).inject(this)
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

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
        subscriptions.clear()
        super.onDetachedFromWindow()
    }

    private fun loadData() {
        Timber.d("loadData() called")

        val trailData = trailConditionsService.trailData()
                .retryOnUnsuccessfulResult(3)
                .retryWithTimeout()
                .cache()

        subscriptions += trailData.filter { result -> result.isSuccessful }
                .map { result -> result.data }
                .toObservable()
                .flatMapIterable { it }
                .map { data -> data.toViewModel() }
                .toList()
                .map { trails -> trails.sortedBy { it.name } }
                .observeOnMainThread()
                .subscribe { trails ->
                    trailListAdapter.trails = trails
                    animator.displayedChildId = R.id.trail_list_content
                }

        subscriptions += trailData.filter { result -> result.isNotSuccessful }
                .observeOnMainThread()
                .doOnSuccess { result ->
                    if (result.isError) {
                        Timber.e(result.error(), "Failed to get trail data from v3 api")
                    } else {
                        val response = result.response()
                        Timber.e("Failed to get trail data from v3 api. Server returned %d", response.code())
                    }
                }
                .subscribe {  animator.displayedChildId = R.id.trail_list_error }

        val trailRegions = trailData.filter { it.isNotSuccessful }
                .flatMap {
                    trailConditionsService.trailRegions()
                            .retryOnUnsuccessfulResult(3)
                            .retryWithTimeout()
                            .toMaybe()
                }
                .cache()

        subscriptions += trailRegions.filter { result -> result.isSuccessful }
                .map { result -> result.data }
                .toObservable()
                .flatMapIterable { it }
                .flatMapIterable { trailRegion -> trailRegion.trails }
                .filter { trailInfo -> trailInfo.lastUpdated.isAfter(THREE_MONTHS_BEFORE_TODAY) }
                .map { trailInfo -> trailInfo.toViewModel() }
                .toList()
                .filter { it.isNotEmpty() }
                .map { trails -> trails.sortedBy { it.name } }
                .observeOnMainThread()
                .subscribe { trailInfoList ->
                    trailListAdapter.trails = trailInfoList
                    animator.displayedChildId = R.id.trail_list_content
                }

        subscriptions += trailRegions.filter { result -> result.isNotSuccessful }
                .observeOnMainThread()
                .doOnSuccess { result ->
                    if (result.isError) {
                        Timber.e(result.error(), "Failed to get trail regions")
                    } else {
                        val response = result.response()
                        Timber.e("Failed to get trail regions. Server returned %d", response.code())
                    }
                }
                .subscribe {  animator.displayedChildId = R.id.trail_list_error }

        refreshLayout.isRefreshing = false
    }
}
