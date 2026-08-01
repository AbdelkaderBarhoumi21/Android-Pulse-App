package com.example.pulse_app.features.task.domain.model
import java.time.Instant

data class TaskModel(
    val id: String,
    val title: String,
    val description: String,
    val priority: TaskPriority,
    val status: TaskStatus,
    val createdAt: Instant,
    val completedAt: Instant? = null,
)