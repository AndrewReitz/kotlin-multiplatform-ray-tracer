package cash.andrew.mntrailconditions.util

import androidx.annotation.DrawableRes
import cash.andrew.mntrailconditions.R
import java.util.*

@DrawableRes
fun statusToResource(status: String): Int {
    val currentStatus = status.toLowerCase(Locale.US)
    return when {
        "wet" in currentStatus -> R.drawable.wet
        "closed" in currentStatus || "not" in currentStatus -> R.drawable.closed
        "damp" in currentStatus -> R.drawable.damp
        "tacky" in currentStatus -> R.drawable.tacky
        "fat tires" in currentStatus || "needs packing" in currentStatus -> R.drawable.fat_tires
        "open" in status || "dry" in currentStatus || "packed" in currentStatus -> R.drawable.dry
        else -> R.drawable.closed
    }
}
