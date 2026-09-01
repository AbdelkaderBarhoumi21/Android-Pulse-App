package com.example.pulse_app.features.task.presentation.logic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pulse_app.core.result.AppResult
import com.example.pulse_app.core.routing.Routes
import com.example.pulse_app.features.task.domain.model.TaskPriority
import com.example.pulse_app.features.task.domain.model.TaskStatus
import com.example.pulse_app.features.task.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTask: GetTaskUseCase,
    private val createTask: CreateTaskUseCase,
    private val updateTask: UpdateTaskUseCase,
) : ViewModel() {

    private val taskId: String? = savedStateHandle[Routes.TaskEdit.ARG]
    private val _uiState = MutableStateFlow(TaskEditUiState(isEditing = taskId != null))
    val uiState: StateFlow<TaskEditUiState> = _uiState.asStateFlow()

    init {
        taskId?.let { id ->
            viewModelScope.launch {
                getTask(id).filterNotNull().first().let { task ->
                    _uiState.update {
                        it.copy(
                            title = task.title,
                            description = task.description,
                            priority = task.priority,
                            status = task.status
                        )
                    }
                }
            }
        }
    }

    fun onTitleChange(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun onPriorityChange(priority: TaskPriority) {
        _uiState.update { it.copy(priority = priority) }
    }

    fun onStatusChange(status: TaskStatus) {
        _uiState.update { it.copy(status = status) }
    }

    fun save() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        val result = if (taskId == null) {
            createTask(
                CreateTaskParams(
                    title = _uiState.value.title,
                    description = _uiState.value.description,
                    priority = _uiState.value.priority,
                    status = _uiState.value.status
                )
            )
        } else {
            updateTask(
                UpdateTaskParams(
                    id = taskId,
                    title = _uiState.value.title,
                    description = _uiState.value.description,
                    priority = _uiState.value.priority,
                    status = _uiState.value.status
                )
            )
        }

        if (result is AppResult.Success) {
            _uiState.update { it.copy(isLoading = false, isSaved = true) }
        } else if (result is AppResult.Error) {
            _uiState.update { it.copy(isLoading = false, errorMessage = result.failure.message) }
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
