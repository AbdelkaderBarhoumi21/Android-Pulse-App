package com.example.pulse_app.features.task.data.datasource.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateTaskRequestDto(
    val title: String,
    val description: String,
    val priority: String,
    val status: String? = null,
)