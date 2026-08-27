package com.example.pulse_app.features.task.presentation.logic

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pulse_app.core.result.AppResult
import com.example.pulse_app.core.usecase.NoParams
import com.example.pulse_app.features.task.domain.usecase.ObserveTasksUseCase
import com.example.pulse_app.features.task.domain.usecase.RefreshTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel
    @Inject
    constructor(
        observeTasks: ObserveTasksUseCase,
        private val refreshTasks: RefreshTasksUseCase,
    ) : ViewModel() {
        private val refreshing = MutableStateFlow<Boolean>(false)
        private val error = MutableStateFlow<String?>(null)
        val uiState: StateFlow<TaskListUiState> =
            combine(
                observeTasks(NoParams),
                refreshing,
                error,
            ) { tasks, refreshing, error ->
                TaskListUiState(
                    tasks = tasks,
                    isLoading = refreshing,
                    errorMessage = error,
                    isEmpty = tasks.isEmpty() && !refreshing && error == null,
                )
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_0000),
                TaskListUiState(),
            )

        init {
            refresh()
        }

        fun refresh() =
            viewModelScope.launch {
                refreshing.value = true
                error.value = null
                val result = refreshTasks(NoParams)
                if (result is AppResult.Error) error.value = result.failure.message
                refreshing.value = false
            }

        fun dismissError() {
            error.value = null
        }
    }
