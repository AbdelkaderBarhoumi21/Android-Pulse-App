package com.example.pulse_app.core.network

import com.example.pulse_app.core.error.AppException
import kotlinx.serialization.SerializationException
import okio.IOException
import retrofit2.HttpException
import java.net.SocketTimeoutException

suspend fun <T> safeApiCall(block: suspend () -> T)= try{
    block()
}catch (e: SocketTimeoutException){
    throw AppException.TimeoutException(e)
} catch (e: IOException){
    throw AppException.NetworkException(e)
}catch(e: HttpException){
    when(e.code()){
        404 -> throw AppException.NotFoundException()
        422 -> throw AppException.ValidationException(e.response()?.errorBody()?.string())
        in 500 .. 599  -> throw AppException.ServerException(e.code(),e.message())
        else -> throw AppException.ServerException(e.code(),e.message)
    }
}catch (e: SerializationException){
    throw AppException.SerializationException(e)
}catch(e: AppException){
    throw e
} catch (e: Exception){
    throw AppException.UnknownException(e)
}