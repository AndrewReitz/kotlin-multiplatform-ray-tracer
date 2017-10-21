package cash.andrew.mntrailconditions.data.api

import cash.andrew.mntrailconditions.data.okhttp.Api
import cash.andrew.mntrailconditions.data.okhttp.ApiVersionHeaderInterceptor
import groovy.transform.CompileDynamic
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import spock.lang.Specification

import java.lang.Void as Should

@CompileDynamic
class ApiVersionHeaderInterceptorSpec extends Specification {
  Should "add Api-Version header"() {
    given:
    def request = new Request.Builder().with {
      url('http://example.com')
      build()
    }
    def chain = Mock(Interceptor.Chain)
    def classUnderTest = new ApiVersionHeaderInterceptor()

    when:
    classUnderTest.intercept(chain)

    then:
    1 * chain.request() >> request
    1 * chain.proceed(_) >> { Request requestParam ->
      new Response.Builder().with {
        assert requestParam.header('Api-Version') == Api.VERSION
        delegate.request(requestParam)
        protocol(Protocol.HTTP_2)
        code(200)
        build()
      }
    }
  }
}
