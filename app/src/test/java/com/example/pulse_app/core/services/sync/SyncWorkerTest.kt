package com.example.pulse_app.core.services.sync

import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.example.pulse_app.core.error.AppFailure
import com.example.pulse_app.core.result.AppResult
import com.example.pulse_app.features.task.domain.repository.TaskRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

// This test verifies one specific branch: if the push succeeds but the pull fails, the worker must return Result.retry()
@RunWith(RobolectricTestRunner::class)
class SyncWorkerTest {
    @Test fun worker_retries_when_pull_fails() =
        runBlocking {
            val repo = mockk<TaskRepository>()
            coEvery { repo.syncPending() } returns AppResult.Success(Unit)
            coEvery { repo.refreshTasks() } returns
                AppResult.Error(AppFailure.NetworkFailure)

            val worker =
                TestListenableWorkerBuilder<SyncWorker>(
                    ApplicationProvider.getApplicationContext(),
                ).setWorkerFactory(
                    object : androidx.work.WorkerFactory() {
                        override fun createWorker(
                            c: android.content.Context,
                            n: String,
                            p: androidx.work.WorkerParameters,
                        ) = SyncWorker(c, p, repo)
                    },
                ).build()

            assertThat(worker.doWork()).isEqualTo(ListenableWorker.Result.retry())
        }
}
