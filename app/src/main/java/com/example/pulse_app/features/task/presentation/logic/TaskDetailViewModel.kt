package com.example.pulse_app.features.task.presentation.logic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pulse_app.core.result.AppResult
import com.example.pulse_app.core.routing.Routes
import com.example.pulse_app.features.task.domain.usecase.DeleteTaskUseCase
import com.example.pulse_app.features.task.domain.usecase.GetTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        getTask: GetTaskUseCase,
        private val deleteTask: DeleteTaskUseCase,
    ) : ViewModel() {
        private val taskId: String = checkNotNull(savedStateHandle[Routes.TaskDetail.ARG])
        private val _isLoading = MutableStateFlow(false)
        private val _error = MutableStateFlow<String?>(null)
        private val _isDeleted = MutableStateFlow(false)

        val uiState: StateFlow<TaskDetailUiState> =
            combine(
                getTask(taskId),
                _isLoading,
                _error,
                _isDeleted,
            ) { task, isLoading, error, isDeleted ->
                TaskDetailUiState(
                    task = task,
                    isLoading = isLoading,
                    errorMessage = error,
                    isDeleted = isDeleted,
                )
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                TaskDetailUiState(isLoading = true),
            )

        fun delete() =
            viewModelScope.launch {
                _isLoading.value = true
                _error.value = null
                val result = deleteTask(taskId)
                if (result is AppResult.Success) {
                    _isDeleted.value = true
                } else if (result is AppResult.Error) {
                    _error.value = result.failure.message
                }
                _isLoading.value = false
            }

        fun dismissError() {
            _error.value = null
        }
    }
