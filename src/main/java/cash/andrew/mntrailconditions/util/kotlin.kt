package cash.andrew.mntrailconditions.util

import timber.log.Timber

suspend fun <T> retry(times: Int = 3, block: suspend () -> T): T {
    repeat(times - 1) {
        try {
            return block()
        } catch (e: Exception) {
            Timber.d(e, "Retrying again")
        }
    }

    Timber.d("final retry")
    return block()
}
