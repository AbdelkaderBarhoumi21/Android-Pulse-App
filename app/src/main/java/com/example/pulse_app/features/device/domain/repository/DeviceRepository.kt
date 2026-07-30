package com.example.pulse_app.features.device.domain.repository

import com.example.pulse_app.core.result.AppResult
import com.example.pulse_app.features.device.domain.model.DeviceModel

interface DeviceRepository {
    suspend fun registerToken(token:String): AppResult<DeviceModel>
    suspend fun unregister(deviceId:String): AppResult<Unit>
    suspend fun cachedDeviceId():String?
}