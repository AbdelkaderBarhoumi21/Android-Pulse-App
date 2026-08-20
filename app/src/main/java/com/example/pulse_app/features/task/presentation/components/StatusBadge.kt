package com.example.pulse_app.features.task.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.pulse_app.core.theme.*
import com.example.pulse_app.features.task.domain.model.TaskStatus

@Composable
fun StatusBadge(
    status: TaskStatus,
    modifier: Modifier = Modifier,
) {
    val (label, ContainerColor, contentColor) =
        when (status) {
            TaskStatus.PENDING -> Triple("Pending", StatusPending, Slate500)
            TaskStatus.IN_PROGRESS -> Triple("In progress", StatusInProgress, Slate500)
            TaskStatus.COMPLETED -> Triple("Completed", StatusCompleted, Slate500)
        }

    Text(
        text = label,
        color = contentColor,
        fontSize = 12.sp,
        modifier =
            modifier
                .background(
                    color = ContainerColor,
                    shape = RoundedCornerShape(AppDimens.radiusSm),
                ).padding(horizontal = AppDimens.spaceSm, vertical = AppDimens.spaceXs),
    )
}

@Preview(showBackground = true)
@Composable
private fun StatusBadgePreview() {
    androidx.compose.foundation.layout.Row(
        horizontalArrangement =
            androidx.compose.foundation.layout.Arrangement
                .spacedBy(AppDimens.spaceSm),
    ) {
        StatusBadge(status = TaskStatus.PENDING)
        StatusBadge(status = TaskStatus.IN_PROGRESS)
        StatusBadge(status = TaskStatus.COMPLETED)
    }
}
