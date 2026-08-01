package com.example.pulse_app.features.task.data.datasource.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TaskDto(
    val id: String,
    val title: String,
    val description: String,
    val priority: String,
    val status: String,
    val createdAt: String,
    val completedAt: String? = null,
)