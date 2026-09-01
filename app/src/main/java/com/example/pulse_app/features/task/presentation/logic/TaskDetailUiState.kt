package com.example.pulse_app.features.task.presentation.logic

import com.example.pulse_app.features.task.domain.model.TaskModel

data class TaskDetailUiState(
    val task: TaskModel? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isDeleted: Boolean = false,
)
