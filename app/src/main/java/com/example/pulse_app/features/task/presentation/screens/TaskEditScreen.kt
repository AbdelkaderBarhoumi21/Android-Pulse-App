package com.example.pulse_app.features.task.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pulse_app.core.theme.AppDimens
import com.example.pulse_app.core.theme.components.AppTextField
import com.example.pulse_app.core.theme.components.LoadingOverlay
import com.example.pulse_app.core.theme.components.PrimaryButton
import com.example.pulse_app.core.utils.AppStrings
import com.example.pulse_app.features.task.domain.model.TaskPriority
import com.example.pulse_app.features.task.domain.model.TaskStatus
import com.example.pulse_app.features.task.presentation.logic.TaskEditViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditScreen(
    onBackClick: () -> Unit,
    viewModel: TaskEditViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            onBackClick()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isEditing) AppStrings.EDIT_TASK else AppStrings.NEW_TASK) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppDimens.spaceMd)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(AppDimens.spaceMd)
            ) {
                AppTextField(
                    value = state.title,
                    onValueChange = viewModel::onTitleChange,
                    label = AppStrings.TITLE_LABEL,
                    singleLine = true
                )

                AppTextField(
                    value = state.description,
                    onValueChange = viewModel::onDescriptionChange,
                    label = AppStrings.DESCRIPTION_LABEL,
                    maxLines = 5
                )

                Text(AppStrings.PRIORITY_LABEL, style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(AppDimens.spaceSm)) {
                    TaskPriority.entries.forEach { priority ->
                        FilterChip(
                            selected = state.priority == priority,
                            onClick = { viewModel.onPriorityChange(priority) },
                            label = { Text(priority.name) }
                        )
                    }
                }

                Text(AppStrings.STATUS_LABEL, style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(AppDimens.spaceSm)) {
                    TaskStatus.entries.forEach { status ->
                        FilterChip(
                            selected = state.status == status,
                            onClick = { viewModel.onStatusChange(status) },
                            label = { Text(status.name) }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                PrimaryButton(
                    text = AppStrings.SAVE,
                    onClick = viewModel::save
                )
            }

            LoadingOverlay(isLoading = state.isLoading)

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
