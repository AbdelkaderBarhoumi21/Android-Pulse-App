package com.example.pulse_app.features.device.data.datasource.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DeviceDto(
    val id:String,
    val token:String,
    val platform:String,
    val createdAt:String,
    val lastSeenAt:String,
)