package com.example.pulse_app.core.di

import com.example.pulse_app.features.device.data.repository.DeviceRepositoryImpl
import com.example.pulse_app.features.device.domain.repository.DeviceRepository
import com.example.pulse_app.features.task.data.repository.TaskRepositoryImpl
import com.example.pulse_app.features.task.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository

    @Binds
    @Singleton
    abstract fun bindDeviceRepository(impl: DeviceRepositoryImpl): DeviceRepository
}
