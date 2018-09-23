package cash.andrew.mntrailconditions.data.moshi.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.threeten.bp.Instant

@Suppress("unused")
class InstantJsonAdapter {
    @FromJson fun instantFromJson(json: Long): Instant = Instant.ofEpochMilli(json)
    @ToJson fun instantToJson(instant: Instant) = instant.toEpochMilli()
}
