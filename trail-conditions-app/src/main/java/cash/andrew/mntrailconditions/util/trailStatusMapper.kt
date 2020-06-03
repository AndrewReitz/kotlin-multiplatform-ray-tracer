package cash.andrew.mntrailconditions.util

import android.content.Context
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import cash.andrew.mntrailconditions.R
import java.util.*

@ColorInt
fun statusToColor(context: Context, status: String): Int {
    val currentStatus = status.toLowerCase(Locale.US)
    val typedValue = TypedValue()

    val value = when {
        "wet" in currentStatus || "closed" in currentStatus ||
                "not" in currentStatus -> R.attr.colorError
        "damp" in currentStatus || "tacky" in currentStatus ||
                "fat tires" in currentStatus || "needs packing" in currentStatus ||
                "open" in currentStatus || "dry" in currentStatus ||
                "packed" in currentStatus -> R.attr.colorPrimary
        else -> R.attr.colorError
    }

    context.theme.resolveAttribute(value, typedValue, true)

    return typedValue.data
}

@DrawableRes
fun statusToResource(status: String): Int {
    val currentStatus = status.toLowerCase(Locale.US)
    return when {
        "wet" in currentStatus -> R.drawable.wet
        "closed" in currentStatus || "not" in currentStatus -> R.drawable.closed
        "damp" in currentStatus -> R.drawable.damp
        "tacky" in currentStatus -> R.drawable.tacky
        "fat tires" in currentStatus || "needs packing" in currentStatus -> R.drawable.fat_tires
        "open" in currentStatus || "dry" in currentStatus || "packed" in currentStatus -> R.drawable.dry
        else -> R.drawable.closed
    }
}
