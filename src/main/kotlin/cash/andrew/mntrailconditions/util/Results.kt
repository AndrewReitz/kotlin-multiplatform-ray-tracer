package cash.andrew.mntrailconditions.util

import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import retrofit2.adapter.rxjava2.Result
import timber.log.Timber

val <T : Any> Result<T>.isSuccessful: Boolean get() = !isError && response().isSuccessful
val <T : Any> Result<T>.isNotSuccessful: Boolean get() = !isSuccessful

val <T : Any> Result<T>.data: T get() = response().body()

/**
 * Retries the call of the single until there is a successful result or it has tried the number of
 * times specified.
 *
 * Note: this will return the first unsuccessful result if all retries fail. IE. if you got a 400
 * error than a 500 error the 400 would be the error the subscriber would see.
 */
fun <A : Any> Single<Result<A>>.retryOnUnsuccessfulResult(times: Long = 1): Single<Result<A>> {
    val cached = this.cache()
    return Flowable.concat(cached.toFlowable(), this.repeat(times))
            .doOnNext { Timber.v("retryOnUnsuccessfulResult() got: %s", it) }
            .takeUntil { it.isSuccessful }
            .filter { it.isSuccessful }
            .singleElement()
            .switchIfEmpty(cached.toMaybe())
            .toSingle()
}

/**
 * Retries the call of the single until there is a successful result or it has tried the number of
 * times specified.
 *
 * Note: this will return the first unsuccessful result if all retries fail. IE. if you got a 400
 * error than a 500 error the 400 would be the error the subscriber would see.
 */
fun <A : Any> Maybe<Result<A>>.retryOnUnsuccessfulResult(times: Long = 1): Maybe<Result<A>> {
    val cached = this.cache()
    return Flowable.concat(cached.toFlowable(), this.repeat(times))
            .doOnNext { Timber.v("retryOnUnsuccessfulResult() got: %s", it) }
            .takeUntil { it.isSuccessful }
            .filter { it.isSuccessful }
            .singleElement()
            .switchIfEmpty(cached)
}
