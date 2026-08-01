package com.example.pulse_app.features.device.data.datasource.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterDeviceRequestDto (
    val token:String,
    val platform: String
)