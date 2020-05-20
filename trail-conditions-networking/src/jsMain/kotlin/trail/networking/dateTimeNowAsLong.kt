package trail.networking

import kotlin.js.Date

actual fun dateTimeNowAsLong(): Long = Date.now().toLong()
