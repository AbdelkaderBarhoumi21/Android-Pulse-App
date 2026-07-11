package com.example.pulse_app.core.network

import com.example.pulse_app.core.error.AppException
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
class SafeApiCallTest{
    @Test(expected = AppException.NetworkException::class)
    fun `IOException becomes network exception`() = runTest {
        safeApiCall <Unit>{throw IOException() }
    }

    @Test(expected = AppException.TimeoutException::class)
    fun `SocketTimeout becomes Timeout`()= runTest{
        safeApiCall <Unit>{ throw AppException.TimeoutException() }
    }
    @Test(expected = AppException.NotFoundException::class)
    fun `http 404 becomes NotFound`() = runTest {
        safeApiCall<Unit> {
            throw HttpException(Response.error<Unit>(404, "".toResponseBody("application/json".toMediaType())))
        }
    }
    @Test(expected = AppException.ValidationException::class)
    fun `http 422 becomes Validation`() = runTest {
        safeApiCall<Unit> {
            throw HttpException(Response.error<Unit>(422, "{}".toResponseBody("application/json".toMediaType())))
        }
    }

    @Test
    fun `success pass values through`()=runTest {
        assertThat(safeApiCall { 42 }).isEqualTo(42)
    }

}