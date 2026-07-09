package com.example.pulse_app.core.network

import okhttp3.Interceptor
import okhttp3.Response

/** Add bearer token here once the API has auth. No-op for now. */
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            // .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}