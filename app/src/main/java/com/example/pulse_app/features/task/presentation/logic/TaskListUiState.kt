package com.example.pulse_app.features.task.presentation.logic

import com.example.pulse_app.features.task.domain.model.TaskModel

data class TaskListUiState(
    val tasks: List<TaskModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isEmpty: Boolean = false,
)
