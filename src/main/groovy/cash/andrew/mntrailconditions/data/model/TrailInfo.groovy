package cash.andrew.mntrailconditions.data.model

import groovy.transform.Immutable
import groovy.transform.ToString
import org.threeten.bp.LocalDateTime

@Immutable
@ToString(includeNames = true)
class TrailInfo {
  String name
  String status
  String description
  LocalDateTime lastUpdated
}

