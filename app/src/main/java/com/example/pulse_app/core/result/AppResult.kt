package com.example.pulse_app.core.result

import com.example.pulse_app.core.error.AppFailure
// interface  => it has zero properties, zero shared behavior, zero constructor parameters of its own.
// class      => can hold constructor parameters, actual stored state, and a real constructor body(AppException)
//  extend one class, but implement many interfaces
// when branch confirms this is AppResult.Success, Kotlin knows, within that branch, that this is specifically a Success so need for this.data direclty data
// is AppResult.Error -> this  => this ref to sealead class AppResult<Nothing>
sealed interface AppResult<out T>{
    data class Success<out T>(val data:T): AppResult<T>
    data class Error(val failure: AppFailure): AppResult<Nothing>
}

inline fun <T, R> AppResult<T>.map(transform: (T) -> R): AppResult<R> = when (this) {
    is AppResult.Success -> AppResult.Success(transform(data))
    is AppResult.Error -> this
}

inline fun <T> AppResult<T>.onSuccess(block: (T) -> Unit): AppResult<T> =
    also { if (this is AppResult.Success) block(data) }

inline fun <T> AppResult<T>.onError(block: (AppFailure) -> Unit): AppResult<T> =
    also { if (this is AppResult.Error) block(failure) }
// try to treat this as an AppResult.Success. If it actually is one, give me that (correctly typed as Success).
// If it's actually an Error instead, don't crash — just give me null.
// ?. means: " if the thing on the left isn't null, go ahead and access .data on it. If it is null, don't even try
fun <T> AppResult<T>.getOrNull():T?= (this as? AppResult.Success)?.data




/*
inline fun <T>AppResult<T>.onError2(block:(AppFailure)->Unit): AppResult<T> {
    if (this is AppResult.Error){
        block(this.failure)
    }
    return this // ← this is exactly what `also` does automatically for you
}

 */