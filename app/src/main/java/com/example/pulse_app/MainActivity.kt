package com.example.pulse_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pulse_app.core.routing.AppNavGraph
import com.example.pulse_app.core.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val startTaskId = intent?.getStringExtra("taskId") // from notification tap
        setContent {
            AppTheme {
                AppNavGraph(
                    startTaskId = startTaskId,
                )
            }
        }
    }
}
