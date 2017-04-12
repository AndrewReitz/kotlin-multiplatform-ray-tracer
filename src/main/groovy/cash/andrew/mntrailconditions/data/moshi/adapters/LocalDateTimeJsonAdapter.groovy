package cash.andrew.mntrailconditions.data.moshi.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import groovy.transform.CompileStatic
import org.threeten.bp.LocalDateTime;

@CompileStatic
class LocalDateTimeJsonAdapter {
  @FromJson LocalDateTime localDateTimeFromJson(String json) {
    return LocalDateTime.parse(json)
  }

  @ToJson String localDateTimeToJson(LocalDateTime localDateTime) {
    return localDateTime.toString()
  }
}
