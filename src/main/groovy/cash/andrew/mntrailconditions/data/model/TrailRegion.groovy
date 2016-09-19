package cash.andrew.mntrailconditions.data.model

import groovy.transform.CompileStatic
import groovy.transform.Immutable
import groovy.transform.ToString

@CompileStatic
@Immutable
@ToString(includeNames = true)
class TrailRegion {
  String name
  List<TrailInfo> trails
}
