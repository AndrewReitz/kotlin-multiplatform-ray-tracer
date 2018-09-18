package cash.andrew.mntrailconditions.ui.trails

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.data.api.TrailConditionsService
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
import kotlinx.android.synthetic.main.trail_list_view.view.*
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import javax.inject.Inject

val THREE_MONTHS_BEFORE_TODAY: LocalDateTime = LocalDateTime.now().minusMonths(3)

class TrailListView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    @Inject lateinit var trailConditionsService: TrailConditionsService
    @Inject lateinit var trailListAdapter: TrailListAdapter

    private val recyclerView get() = trail_list_recycler_view
    private val refreshLayout get() = trail_list_content
    private val animator get() = trail_list_animator

    var favoriteTrailsPref: Preference<Set<String>>? = null

    init {
        context.activityComponent.trailsComponent.inject(this)
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
                .cache()

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
                .cache()

        val trailsV2 = trailRegions.filter { result -> result.isSuccessful }
                .map { result -> result.data }
                .toObservable()
                .flatMapIterable { it }
                .filter { trail -> trail.lastUpdated.isAfter(THREE_MONTHS_BEFORE_TODAY) }
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
                .subscribe { trails ->
                    if (favoriteTrailsPref != null && trails.isEmpty()) {
                        animator.displayedChildId = R.id.trail_list_no_favorites_text
                    } else {
                        trailListAdapter.trails = trails
                        animator.displayedChildId = R.id.trail_list_recycler_view
                    }
                }

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
                .subscribe { animator.displayedChildId = R.id.trail_list_error }

        refreshLayout.isRefreshing = false
    }
}
