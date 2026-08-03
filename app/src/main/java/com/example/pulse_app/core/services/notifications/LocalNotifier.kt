package com.example.pulse_app.core.services.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.pulse_app.MainActivity
import com.example.pulse_app.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalNotifier
    @Inject
    constructor(
        @param:ApplicationContext private val context: Context,
    ) {
        fun show(payload: NotificationPayload) {
            // POST_NOTIFICATIONS runtime permission (Android 13+) must already be granted.
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) != PackageManager.PERMISSION_GRANTED &&
                android.os.Build.VERSION.SDK_INT >= 33
            ) {
                return
            }

            val intent =
                Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    payload.taskId?.let { putExtra("taskId", it) }
                }
            val pendingIntent =
                PendingIntent.getActivity(
                    context,
                    payload.taskId.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                )
            val notification =
                NotificationCompat.Builder(context, NotificationChannels.TASKS)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(payload.title)
                    .setContentText(payload.body)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build()

            NotificationManagerCompat.from(context).notify(
                payload.taskId.hashCode(),
                notification
            )
        }
    }
