@file:Suppress("NOTHING_TO_INLINE")

package cash.andrew.mntrailconditions.util

inline fun String.toTopicName() = spacesToHyphens()
inline fun String.spacesToHyphens() = replace(" ", "-")
