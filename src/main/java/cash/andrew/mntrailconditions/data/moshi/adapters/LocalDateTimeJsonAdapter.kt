package cash.andrew.mntrailconditions.data.moshi.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

import org.threeten.bp.LocalDateTime

@SuppressWarnings("unused")
class LocalDateTimeJsonAdapter {
    @FromJson fun localDateTimeFromJson(json: String) = LocalDateTime.parse(json)
    @ToJson fun localDateTimeToJson(localDateTime: LocalDateTime) = localDateTime.toString()
}
