package com.example.pulse_app.core.error

import com.example.pulse_app.core.utils.AppStrings

sealed class AppFailure(open val message: String? = null) {
    data object NetworkFailure : AppFailure(AppStrings.ERR_NETWORK)
    data object ServerFailure : AppFailure(AppStrings.ERR_SERVER)
    data object NotFoundFailure : AppFailure(AppStrings.ERR_NOT_FOUND)
    data class ValidationFailure(override val message: String = AppStrings.ERR_VALIDATION) : AppFailure(message)
    data object CacheFailure : AppFailure(AppStrings.ERR_UNKNOWN)
    data class UnknownFailure(override val message: String = AppStrings.ERR_UNKNOWN) : AppFailure(message)
}