package cash.andrew.mntrailconditions.ui.trails

import android.content.Context
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.widget.LinearLayout
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.data.Funcs
import cash.andrew.mntrailconditions.data.Injector
import cash.andrew.mntrailconditions.data.api.Results
import cash.andrew.mntrailconditions.data.api.TrailConditionsService
import cash.andrew.mntrailconditions.data.model.TrailInfo
import cash.andrew.mntrailconditions.data.model.TrailRegion
import cash.andrew.mntrailconditions.ui.misc.BetterViewAnimator
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import groovy.transform.CompileStatic
import rx.functions.Func1

import javax.inject.Inject
import retrofit2.adapter.rxjava.Result
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber

@CompileStatic
class TrailListView extends LinearLayout {

  @InjectView(R.id.trail_list_toolbar) Toolbar toolbarView
  @InjectView(R.id.trail_list_animator) BetterViewAnimator animator

  @Inject TrailConditionsService trailConditionsService

  TrailListView(Context context, AttributeSet attrs) {
    super(context, attrs)
    if (!isInEditMode()) {
      Injector.obtain(context).inject(this)
    }
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate()
    SwissKnife.inject(this)
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow()

    Observable<Result<List<TrailRegion>>> result = trailConditionsService.trailRegions
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .share()

    result.filter(Results.isSuccessful())
          .map(Results.resultToBodyData())
          .flatMap {List<TrailRegion> trailRegions -> Observable.from(trailRegions) }
          .map { TrailRegion trailRegion -> trailRegion.trails }
          .flatMap { List<TrailInfo> trailInfoList -> Observable.from(trailInfoList) }
          .filter { TrailInfo ti -> ti.name != 'Afton Alps' }
          .toList()
          .subscribe { List<TrailInfo> trailInfo ->
            animator.displayedChildId = R.id.trail_lis_content
          }

    result.filter(Funcs.not(Results.isSuccessful()))
        .doOnNext(Results.logError())
        .subscribe {
          animator.displayedChildId = R.id.trail_list_error
        }
  }
}
