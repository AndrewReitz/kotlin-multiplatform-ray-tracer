package cash.andrew.mntrailconditions.data.okhttp

import groovy.transform.CompileStatic
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

@CompileStatic
class ApiVersionHeaderInterceptor implements Interceptor {
  @Override Response intercept(Interceptor.Chain chain) throws IOException {
    Request newRequest = chain.request().newBuilder().with {
      addHeader('Api-Version', Api.VERSION)
      build()
    }
    return chain.proceed(newRequest)
  }
}
