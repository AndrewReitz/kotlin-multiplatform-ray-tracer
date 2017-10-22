package cash.andrew.mntrailconditions.data.api

import cash.andrew.mntrailconditions.BuildConfig
import cash.andrew.mntrailconditions.data.okhttp.UserAgentInterceptor
import groovy.transform.CompileDynamic
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import spock.lang.Specification

import java.lang.Void as Should

@CompileDynamic
class UserAgentInterceptorSpec extends Specification {

  Should "add User-Agent header"() {
    given:
    def request = new Request.Builder().with {
      url('http://example.com')
      build()
    }
    def chain = Mock(Interceptor.Chain)
    def classUnderTest = new UserAgentInterceptor()

    when:
    classUnderTest.intercept(chain)

    then:
    1 * chain.request() >> request
    1 * chain.proceed(_) >> { Request requestParam ->
      new Response.Builder().with {
        assert requestParam.header('User-Agent') == "${BuildConfig.APPLICATION_ID}.${BuildConfig.VERSION_CODE}"
        delegate.request(requestParam)
        protocol(Protocol.HTTP_2)
        code(200)
        build()
      }
    }
  }
}
