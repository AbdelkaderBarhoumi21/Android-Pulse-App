package com.example.pulse_app.features.task.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.pulse_app.core.theme.AppDimens
import com.example.pulse_app.core.utils.AppStrings

@Composable
fun ErrorView(
    message: String,
    onRetry: () -> Unit,
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(message)
        Spacer(Modifier.height(AppDimens.spaceMd))
        Button(onClick = onRetry) { Text(AppStrings.RETRY) }
    }
}
