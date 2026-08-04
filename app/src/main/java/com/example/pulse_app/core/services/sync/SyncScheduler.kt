package com.example.pulse_app.core.services.sync

import android.content.Context
import androidx.work.*
import com.example.pulse_app.core.utils.AppConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

class SyncScheduler
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) {
        private val networkConstraint = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        /** One-off sync (FCM push, manual refresh). */
        fun scheduleImmediateSync() {
            val request =
                OneTimeWorkRequestBuilder<SyncWorker>()
                    .setConstraints(
                        networkConstraint,
                    ).setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
                    .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                "${AppConstants.SYNC_WORK_NAME}_now",
                ExistingWorkPolicy.REPLACE,
                request,
            )
        }

        /** Periodic safety-net sync (e.g. every 30 min). */
        fun schedulePeriodicSync() {
            val request = PeriodicWorkRequestBuilder<SyncWorker>(30, TimeUnit.MINUTES).setConstraints(networkConstraint).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                AppConstants.SYNC_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request,
            )
        }
    }
