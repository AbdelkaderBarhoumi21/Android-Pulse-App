package com.example.pulse_app.features.task.data.datasource.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateTaskRequestDto(
    val title: String? = null,
    val description: String? = null,
    val priority: String? = null,
    val status: String? = null,
)