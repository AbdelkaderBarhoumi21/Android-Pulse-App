package com.example.pulse_app.features.device.domain.usecase

import com.example.pulse_app.core.result.AppResult
import com.example.pulse_app.core.usecase.UseCase
import com.example.pulse_app.features.device.domain.model.DeviceModel
import com.example.pulse_app.features.device.domain.repository.DeviceRepository
import javax.inject.Inject

class RegisterDeviceUseCase @Inject constructor(private val repo: DeviceRepository): UseCase<String, DeviceModel> {
    override suspend fun invoke(params: String): AppResult<DeviceModel> = repo.registerToken(token = params)
}