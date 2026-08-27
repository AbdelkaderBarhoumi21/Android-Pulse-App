package com.example.pulse_app.features.task.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pulse_app.core.theme.AppDimens
import com.example.pulse_app.core.utils.AppStrings
import com.example.pulse_app.features.task.presentation.components.EmptyState
import com.example.pulse_app.features.task.presentation.components.ErrorView
import com.example.pulse_app.features.task.presentation.components.TaskCard
import com.example.pulse_app.features.task.presentation.logic.TaskListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    onTaskClick: (String) -> Unit,
    onAddClick: () -> Unit,
    viewModel: TaskListViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = { TopAppBar(title = { Text(AppStrings.TASKS_TITLE) }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
            ) {
                Icon(Icons.Default.Add, contentDescription = AppStrings.NEW_TASK)
            }
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            when {
                state.errorMessage != null && state.tasks.isEmpty() -> {
                    ErrorView(
                        message = state.errorMessage!!,
                        onRetry = viewModel::refresh,
                    )
                }

                state.isEmpty -> {
                    EmptyState(message = AppStrings.EMPTY_TASKS)
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(AppDimens.spaceSm),
                        verticalArrangement = Arrangement.spacedBy(AppDimens.spaceSm),
                    ) {
                        items(
                            state.tasks,
                            key = { it.id },
                        ) { task ->
                            TaskCard(
                                task = task,
                                onClick = { onTaskClick(task.id) },
                            )
                        }
                    }
                }
            }
            if (state.isLoading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}
