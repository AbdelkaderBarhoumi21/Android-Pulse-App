package com.example.pulse_app.features.device.data.repository

import com.example.pulse_app.core.di.IoDispatcher
import com.example.pulse_app.core.error.AppErrorMapper
import com.example.pulse_app.core.network.safeApiCall
import com.example.pulse_app.core.result.AppResult
import com.example.pulse_app.core.utils.AppConstants
import com.example.pulse_app.features.device.data.datasource.local.DeviceTokenStore
import com.example.pulse_app.features.device.data.datasource.remote.DeviceApi
import com.example.pulse_app.features.device.data.datasource.remote.dto.RegisterDeviceRequestDto
import com.example.pulse_app.features.device.domain.model.DeviceModel
import com.example.pulse_app.features.device.domain.repository.DeviceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeviceRepositoryImpl
    @Inject
    constructor(
        private val api: DeviceApi,
        private val localStore: DeviceTokenStore,
        @IoDispatcher private val io: CoroutineDispatcher,
    ) : DeviceRepository {
        override suspend fun registerToken(token: String): AppResult<DeviceModel> =
            withContext(io) {
                try {
                    val dto =
                        safeApiCall {
                            api.register(
                                RegisterDeviceRequestDto(
                                    token = token,
                                    AppConstants.DEVICE_PLATFORM,
                                ),
                            )
                        }

                    localStore.save(dto.id, token)
                    AppResult.Success(DeviceModel(dto.id, dto.token, dto.platform))
                } catch (e: Throwable) {
                    AppResult.Error(AppErrorMapper.map(e))
                }
            }

        override suspend fun unregister(deviceId: String): AppResult<Unit> =
            withContext(io) {
                try {
                    safeApiCall { api.delete(deviceId) }
                    AppResult.Success(Unit)
                } catch (e: Throwable) {
                    AppResult.Error(AppErrorMapper.map(e))
                }
            }

        override suspend fun cachedDeviceId(): String? = localStore.deviceId()
    }
