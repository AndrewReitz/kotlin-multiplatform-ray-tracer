package cash.andrew.mntrailconditions.util

import io.reactivex.Maybe
import io.reactivex.Single
import org.amshove.kluent.mock
import org.amshove.kluent.shouldBe
import org.junit.Test
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result
import java.io.IOException

@Suppress("IllegalIdentifier")
class ResultsTest {
    @Test
    fun `should be successful when the response is a success`() {
        // given
        val result = Result.response(Response.success("Test"))

        // expect
        result.isSuccessful shouldBe true
        result.isNotSuccessful shouldBe false
    }

    @Test
    fun `should not be successful when there is an error or the response has an error`() {
        // given
        val result1 = Result.error<Any>(IOException("Oh no!"))
        val result2 = Result.response(Response.error<String>(400, mock()))

        // expect
        result1.isSuccessful shouldBe false
        result2.isSuccessful shouldBe false
        result1.isNotSuccessful shouldBe true
        result2.isNotSuccessful shouldBe true
    }

    @Test
    fun `(single) should retry on error and then continue when successful`() {
        // given
        val result1 = Result.error<String>(IOException("Oh no!"))
        val result2 = Result.response(Response.success("Test"))

        var time = 0
        val single = Single.create<Result<String>> { emitter ->
            if (time++ == 0) {
                emitter.onSuccess(result1)
                return@create
            }

            emitter.onSuccess(result2)
        }

        // when
        val test = single.retryOnUnsuccessfulResult().test()

        // then
        test.assertValue(result2).assertComplete().assertNoErrors()
    }

    @Test
    fun `(maybe) should retry on error and then continue when successful`() {
        // given
        val result1 = Result.error<String>(IOException("Oh no!"))
        val result2 = Result.response(Response.success("Test"))

        var time = 0
        val maybe = Maybe.create<Result<String>> { emitter ->
            if (time++ == 0) {
                emitter.onSuccess(result1)
                return@create
            }

            emitter.onSuccess(result2)
        }

        // when
        val test = maybe.retryOnUnsuccessfulResult().test()

        // then
        test.assertValue(result2).assertComplete().assertNoErrors()
    }

    @Test
    fun `(maybe) should do nothing when it is empty and uses retryOnUnsuccessfulResult`() {
        // when
        val test = Maybe.empty<Result<String>>().retryOnUnsuccessfulResult().test()

        // then
        test.assertNoErrors().assertComplete().assertNoValues()
    }
}
