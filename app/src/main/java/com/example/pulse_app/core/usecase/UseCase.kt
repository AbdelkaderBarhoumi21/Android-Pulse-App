package com.example.pulse_app.core.usecase

import com.example.pulse_app.core.result.AppResult
import kotlinx.coroutines.flow.Flow

/** One-shot use case. R = result type, P = params type. */
interface UseCase<in P, out R> {
    suspend operator fun invoke(params: P): AppResult<R>
}

/** Streaming use case (e.g. observing Room). */
interface FlowUseCase<in P, out R> {
    operator fun invoke(params: P): Flow<R>
}

/** Marker for use cases that take no input — same as Flutter NoParams. */
object NoParams
