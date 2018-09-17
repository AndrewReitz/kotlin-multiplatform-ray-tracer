@file:Suppress("NOTHING_TO_INLINE")

package cash.andrew.mntrailconditions.util

import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.widget.TooltipCompat

inline fun View.setToolTipTextCompat(@StringRes value: Int): Unit =
        context.getString(value).capitalize()
                .let { TooltipCompat.setTooltipText(this, it) }
