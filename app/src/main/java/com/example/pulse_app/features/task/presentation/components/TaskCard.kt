package com.example.pulse_app.features.task.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.pulse_app.core.theme.AppDimens
import com.example.pulse_app.features.task.domain.model.TaskModel

@Composable
fun TaskCard(
    task: TaskModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            Modifier.padding(AppDimens.spaceMd),
        ) {
            Text(task.title, style = MaterialTheme.typography.titleMedium)
            if (task.description.isNotBlank()) {
                Spacer(Modifier.height(AppDimens.spaceXs))
                Text(task.description, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
            }
            Spacer(Modifier.height(AppDimens.spaceMd))
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppDimens.spaceSm),
            ) {
                PriorityChip(task.priority)
                StatusBadge(task.status)
            }
        }
    }
}
