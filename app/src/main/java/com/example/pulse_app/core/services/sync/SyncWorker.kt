package com.example.pulse_app.core.services.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.pulse_app.core.result.AppResult
import com.example.pulse_app.features.task.domain.repository.TaskRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

// @HiltWorker — telling Hilt "this Worker needs the assisted-injection treatment, wire it into WorkManager's system"
// It generates the actual WorkerFactory implementation that WorkManager needs to know about
// this factory is what makes it possible for WorkManager, when it decides to run SyncWorker,
// to end up with a properly-injected instance (repository included) instead of a broken one missing its dependency.
@HiltWorker
class SyncWorker
    @AssistedInject
    constructor(
        @Assisted context: Context,
        @Assisted params: WorkerParameters,
        private val taskRepository: TaskRepository,
    ) : CoroutineWorker(context, params) {
        override suspend fun doWork(): Result {
            // 1) push local pending changes, 2) pull server truth
            val pushed = taskRepository.syncPending()
            val pulled = taskRepository.refreshTasks()
            return if (pushed is AppResult.Success && pulled is AppResult.Success) Result.success() else Result.retry() // WorkManager backs off and retries when network returns
        }
    }
