package com.example.pulse_app.core.error

import com.google.common.truth.Truth.assertThat
import org.junit.Test


class AppErrorMapperTest{
    @Test fun `Network exception maps to network failure`(){
        assertThat(AppErrorMapper.map(AppException.NetworkException())).isEqualTo(AppFailure.NetworkFailure)
    }

    @Test fun `Timeout maps to timeout failure`(){
        assertThat(AppErrorMapper.map(AppException.TimeoutException())).isEqualTo(AppFailure.NetworkFailure)
    }

    @Test fun `Not found maps to not found failure`(){
        assertThat(AppErrorMapper.map(AppException.NotFoundException())).isEqualTo(AppFailure.NotFoundFailure)
    }

    @Test fun `Validation carries server message`(){
        val result = AppErrorMapper.map(AppException.ValidationException("Title is required"))
        assertThat(result).isInstanceOf(AppFailure.ValidationFailure::class.java)
        assertThat((result as AppFailure.ValidationFailure).message).isEqualTo("Title is required")
    }

    @Test  fun `unmaped throwable maps to unknow`(){
        assertThat(AppErrorMapper.map(RuntimeException("boom"))).isInstanceOf(AppFailure.UnknownFailure::class.java)
    }
}