package com.example.pulse_app.core.result

import com.example.pulse_app.core.error.AppFailure
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AppResultTest{
    @Test fun `map transform success`(){
        val r: AppResult<Int> = AppResult.Success(2)
        assertThat(r.map { it*10 }).isEqualTo(AppResult.Success(20))
    }

    @Test fun `map is identity on Error`(){
        val result: AppResult<Int> = AppResult.Error(AppFailure.NetworkFailure)
        assertThat(result.map { it*10 }).isEqualTo(AppResult.Error(AppFailure.NetworkFailure))

    }

    @Test fun `onSuccess fires only on success`(){
        var hit =0
        AppResult.Success(1).onSuccess { hit ++ }
        (AppResult.Error(AppFailure.ServerFailure) as AppResult<Int>).onSuccess { hit ++ }
        assertThat(hit).isEqualTo(2)
    }

    @Test fun `getOrNull returns data or null`() {
        assertThat(AppResult.Success("x").getOrNull()).isEqualTo("x")
        assertThat((AppResult.Error(AppFailure.ServerFailure) as AppResult<String>).getOrNull()).isNull()
    }
}