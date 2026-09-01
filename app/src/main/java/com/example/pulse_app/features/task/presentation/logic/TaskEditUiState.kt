package com.example.pulse_app.features.task.presentation.logic

import com.example.pulse_app.features.task.domain.model.TaskPriority
import com.example.pulse_app.features.task.domain.model.TaskStatus

data class TaskEditUiState(
    val title: String = "",
    val description: String = "",
    val priority: TaskPriority = TaskPriority.LOW,
    val status: TaskStatus = TaskStatus.PENDING,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSaved: Boolean = false,
    val isEditing: Boolean = false,
)
