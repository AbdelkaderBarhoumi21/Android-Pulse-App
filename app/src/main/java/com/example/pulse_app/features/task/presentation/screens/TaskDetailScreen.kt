package com.example.pulse_app.features.task.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pulse_app.core.theme.AppDimens
import com.example.pulse_app.core.utils.AppStrings
import com.example.pulse_app.features.task.presentation.components.PriorityChip
import com.example.pulse_app.features.task.presentation.components.StatusBadge
import com.example.pulse_app.features.task.presentation.logic.TaskDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit,
    viewModel: TaskDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) {
            onBackClick()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(AppStrings.APP_NAME) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { state.task?.id?.let { onEditClick(it) } }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = viewModel::delete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            state.task?.let { task ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(AppDimens.spaceMd)
                ) {
                    Text(task.title, style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(AppDimens.spaceSm))
                    Row(horizontalArrangement = Arrangement.spacedBy(AppDimens.spaceSm)) {
                        PriorityChip(task.priority)
                        StatusBadge(task.status)
                    }
                    Spacer(modifier = Modifier.height(AppDimens.spaceMd))
                    Text(task.description, style = MaterialTheme.typography.bodyLarge)
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(androidx.compose.ui.Alignment.Center))
            }

            state.errorMessage?.let { error ->
                AlertDialog(
                    onDismissRequest = viewModel::dismissError,
                    title = { Text("Error") },
                    text = { Text(error) },
                    confirmButton = {
                        TextButton(onClick = viewModel::dismissError) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}
