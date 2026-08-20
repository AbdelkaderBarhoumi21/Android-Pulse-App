package com.example.pulse_app.features.task.presentation.components

import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.pulse_app.core.theme.*
import com.example.pulse_app.features.task.domain.model.TaskPriority

@Composable
fun PriorityChip(priority: TaskPriority) {
    val (label, color) =
        when (priority) {
            TaskPriority.LOW -> "Low" to PriorityLow
            TaskPriority.MEDIUM -> "Medium" to PriorityMedium
            TaskPriority.HIGH -> "High" to PriorityHigh
        }
    AssistChip(
        onClick = {},
        label = { Text(label) },
        colors = AssistChipDefaults.assistChipColors(labelColor = color),
    )
}
