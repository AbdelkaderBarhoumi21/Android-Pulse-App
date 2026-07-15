package com.example.pulse_app.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier annotation class IoDispatcher

@Qualifier annotation class DefaultDispatcher

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {
    @Provides @IoDispatcher
    fun io(): CoroutineDispatcher = Dispatchers.IO

    @Provides @DefaultDispatcher
    fun default(): CoroutineDispatcher = Dispatchers.Default
}
