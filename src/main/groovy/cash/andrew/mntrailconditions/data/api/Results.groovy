package cash.andrew.mntrailconditions.data.api

import groovy.transform.CompileStatic
import retrofit2.Response
import retrofit2.adapter.rxjava.Result
import rx.functions.Action1
import rx.functions.Func1
import timber.log.Timber

@CompileStatic
abstract class Results {
  private static final Func1<Result<?>, Boolean> SUCCESSFUL = { Result<?> result ->
    !result.isError() && result.response().isSuccessful()
  } as Func1<Result<?>, Boolean>

  static Func1<Result<?>, Boolean> isSuccessful() {
    return SUCCESSFUL
  }

  static <T> Func1<Result<T>, T> resultToBodyData() {
    return { Result<T> result ->
      return result.response().body()
    } as Func1<Result<T>, T>
  }

  static Action1<Result<?>> logError() {
    return { Result<?> result ->
      if (result.isError()) {
        Timber.e(result.error(), "Failed to get trending repositories")
      } else {
        Response<?> response = result.response()
        Timber.e("Failed to get trending repositories. Server returned %d", response.code())
      }
    } as Action1<Result<?>>
  }
}