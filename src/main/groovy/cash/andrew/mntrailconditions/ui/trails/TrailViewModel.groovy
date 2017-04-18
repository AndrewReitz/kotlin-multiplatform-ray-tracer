package cash.andrew.mntrailconditions.ui.trails

import android.support.annotation.DrawableRes
import cash.andrew.mntrailconditions.R
import cash.andrew.mntrailconditions.data.model.TrailInfo
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber

import static cash.andrew.mntrailconditions.util.Preconditions.checkNotNull

class TrailViewModel {

  private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern('MM/dd/yyyy HH:mm a')

  @Delegate private final TrailInfo trailInfo

  TrailViewModel(TrailInfo trailInfo) {
    this.trailInfo = checkNotNull(trailInfo, 'trailInfo == null')
  }

  String getLastUpdatedFormated() {
    return DATE_TIME_FORMAT.format(lastUpdated)
  }

  @DrawableRes int getResourceId() {
    def currentStatus = status.toLowerCase()

    if (currentStatus.contains('wet')) {
      return R.drawable.wet
    }

    if (currentStatus.contains('closed') || currentStatus.contains('not')) {
      return R.drawable.closed
    }

    if (currentStatus.contains('damp')) {
      return R.drawable.damp
    }

    if (currentStatus.contains('tacky')) {
      return R.drawable.tacky
    }

    if (currentStatus.contains('fat tires')) {
      return R.drawable.fat_tires
    }

    if (currentStatus.contains('dry') || currentStatus.contains('packed')) {
      return R.drawable.dry
    }

    // don't know what else to do so log it and return closed
    Timber.d('Unknown status setting status to closed; %s', trailInfo)
    return R.drawable.closed
  }
}
