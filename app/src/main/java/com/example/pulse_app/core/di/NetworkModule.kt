package com.example.pulse_app.core.di

import com.example.pulse_app.BuildConfig
import com.example.pulse_app.core.network.ApiClientFactory
import com.example.pulse_app.core.network.AuthInterceptor
import com.example.pulse_app.core.utils.AppConstants
import com.example.pulse_app.features.device.data.datasource.remote.DeviceApi
import com.example.pulse_app.features.task.data.datasource.remote.TaskApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun okHttp(): OkHttpClient {
        val logging =
            HttpLoggingInterceptor().apply {
                level =
                    if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
            }
        return OkHttpClient
            .Builder()
            .addInterceptor(AuthInterceptor())
            .addInterceptor(logging)
            .connectTimeout(AppConstants.NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(AppConstants.NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    @Provides @Singleton
    fun retrofit(client: OkHttpClient): Retrofit = ApiClientFactory.retrofit(BuildConfig.BASE_URL, client)

    @Provides
    @Singleton
    fun provideTaskApi(retrofit: Retrofit): TaskApi = retrofit.create(TaskApi::class.java)

    @Provides
    @Singleton
    fun provideDeviceApi(retrofit: Retrofit): DeviceApi = retrofit.create(DeviceApi::class.java)
}
