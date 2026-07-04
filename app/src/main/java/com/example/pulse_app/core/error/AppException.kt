package com.example.pulse_app.core.error

sealed class AppException(message: String? = null, cause: Throwable? = null): Exception(message,cause){
    class NetworkException(cause: Throwable?=null) : AppException(message="Network exception",cause)
    class TimeoutException(cause: Throwable?=null): AppException(message="Timeout exception",cause)
    class ServerException(cause:Throwable?=null): AppException(message = "Server exception",cause)
    class NotFoundException(cause: Throwable?=null): AppException(message = "Not found exception",cause)
    class ValidationException(cause: Throwable?=null): AppException(message = "Validation exception",cause)
    class SerializationException(cause: Throwable?=null): AppException(message = "Serialization exception",cause)
    class CacheException(cause: Throwable?=null): AppException(message = "Local database exception",cause)
    class UnknownException(cause: Throwable?=null): AppException(message = "Unknown exception",cause)
}