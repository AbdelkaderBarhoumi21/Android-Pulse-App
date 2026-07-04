package com.example.pulse_app.core.error

sealed class AppException(message: String? = null, cause: Throwable? = null) : Exception(message, cause) {
    class NetworkException(cause: Throwable? = null) : AppException("Network exception", cause)
    class TimeoutException(cause: Throwable? = null) : AppException("Timeout exception", cause)
    class ServerException(val code: Int = 0, val serverMessage: String? = null) :
        AppException("Server exception $code: $serverMessage")
    class NotFoundException(val resource: String = "Resource") : AppException("$resource not found")
    class ValidationException(val serverMessage: String? = null, cause: Throwable? = null) :
        AppException(serverMessage ?: "Validation exception", cause)
    class SerializationException(cause: Throwable? = null) : AppException("Serialization exception", cause)
    class CacheException(cause: Throwable? = null) : AppException("Local database exception", cause)
    class UnknownException(cause: Throwable? = null) : AppException("Unknown exception", cause)
}