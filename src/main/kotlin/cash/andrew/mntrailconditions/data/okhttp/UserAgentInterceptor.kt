package cash.andrew.mntrailconditions.data.okhttp

import cash.andrew.mntrailconditions.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class UserAgentInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response {
        return with(chain!!) {
            proceed(with(request().newBuilder()) {
                header("User-Agent", "${BuildConfig.APPLICATION_ID}.${BuildConfig.VERSION_CODE}")
                build()
            })
        }
    }
}
