package cash.andrew.mntrailconditions.data.model

import android.support.annotation.DrawableRes
import cash.andrew.mntrailconditions.R
import groovy.transform.CompileStatic
import groovy.transform.Immutable
import groovy.transform.ToString
import org.threeten.bp.LocalDateTime

@CompileStatic
@Immutable(knownImmutableClasses = [LocalDateTime])
@ToString(includeNames = true)
class TrailInfo {
  String name
  String status
  String description
  LocalDateTime lastUpdated

  @DrawableRes final int getResourceId() {
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
  }
}

