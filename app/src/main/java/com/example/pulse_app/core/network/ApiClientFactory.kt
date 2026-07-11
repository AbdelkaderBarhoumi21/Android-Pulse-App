package com.example.pulse_app.core.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object ApiClientFactory{
    val json: Json =Json {
        ignoreUnknownKeys= true
        explicitNulls=false
        encodeDefaults =true
    }

    fun retrofit(baseurl:String,client:okhttp3.OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(baseurl)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType())).build()
}