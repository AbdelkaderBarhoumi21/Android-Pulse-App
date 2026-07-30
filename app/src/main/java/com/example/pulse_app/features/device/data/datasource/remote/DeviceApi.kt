package com.example.pulse_app.features.device.data.datasource.remote

import com.example.pulse_app.core.network.ApiEndpoints
import com.example.pulse_app.features.device.data.datasource.remote.dto.DeviceDto
import com.example.pulse_app.features.device.data.datasource.remote.dto.RegisterDeviceRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface DeviceApi {
    @POST(ApiEndpoints.DEVICES)
    suspend fun register(@Body body: RegisterDeviceRequestDto): DeviceDto

    @DELETE(ApiEndpoints.DEVICE_BY_ID)
    suspend fun delete(@Path(ApiEndpoints.PATH_ID) id: String)

}