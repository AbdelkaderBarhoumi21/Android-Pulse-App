package com.example.pulse_app.core.services.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationChannels {
    const val TASKS = "tasks_channel"

    fun register(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    TASKS,
                    "Task updates",
                    NotificationManager.IMPORTANCE_HIGH,
                ).apply { description = "Notifications about your tasks" }
            context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }
}
