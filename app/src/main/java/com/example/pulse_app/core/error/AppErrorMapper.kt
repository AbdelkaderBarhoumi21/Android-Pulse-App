package com.example.pulse_app.core.error

object AppErrorMapper {
    fun map(throwable: Throwable): AppFailure =  when (throwable) {
        is AppException.NetworkException,
        is AppException.TimeoutException -> AppFailure.NetworkFailure

        is AppException.NotFoundException -> AppFailure.NotFoundFailure

        is AppException.ValidationException -> AppFailure.ValidationFailure(
            throwable.serverMessage ?: AppFailure.ValidationFailure().message
        )

        is AppException.ServerException -> AppFailure.ServerFailure

        is AppException.CacheException -> AppFailure.CacheFailure

        is AppException.SerializationException -> AppFailure.UnknownFailure()

        is AppException.UnknownException -> AppFailure.UnknownFailure()

        else -> AppFailure.UnknownFailure()
    }
}