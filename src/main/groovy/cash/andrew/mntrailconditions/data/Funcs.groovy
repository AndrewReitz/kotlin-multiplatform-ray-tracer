package cash.andrew.mntrailconditions.data

import groovy.transform.CompileStatic
import rx.functions.Func1

@CompileStatic
abstract class Funcs {
  public static <T> Func1<T, Boolean> not(final Func1<T, Boolean> func) {
    return { !func.call(it) } as Func1<T, Boolean>
  }
}
