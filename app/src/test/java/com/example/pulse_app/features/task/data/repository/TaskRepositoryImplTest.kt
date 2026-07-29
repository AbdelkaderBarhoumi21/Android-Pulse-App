package com.example.pulse_app.features.task.data.repository

import app.cash.turbine.test
import com.example.pulse_app.core.enums.SyncState
import com.example.pulse_app.core.result.AppResult
import com.example.pulse_app.features.task.data.datasource.local.TaskDao
import com.example.pulse_app.features.task.data.datasource.local.TaskEntity
import com.example.pulse_app.features.task.data.datasource.remote.TaskApi
import com.example.pulse_app.features.task.data.datasource.remote.dto.TaskDto
import com.example.pulse_app.features.task.domain.model.TaskPriority
import com.example.pulse_app.features.task.domain.model.TaskStatus
import com.google.common.truth.Truth.assertThat
import java.time.Instant


import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.IOException

class TaskRepositoryImplTest {

    private val api = mockk<TaskApi>()
    private val dao = mockk<TaskDao>(relaxed = true)
    private val repo = TaskRepositoryImpl(api, dao, UnconfinedTestDispatcher())

    @Test fun `observeTasks maps entities to domain`() = runTest {
        every { dao.observeTasks(any()) } returns flowOf(
            listOf(TaskEntity("1", "t", "d", "high", "pending", java.time.Instant.now(), null))
        )
        repo.observeTasks().test {
            val tasks = awaitItem()
            assertThat(tasks).hasSize(1)
            assertThat(tasks.first().priority).isEqualTo(TaskPriority.HIGH)
            awaitComplete()
        }
    }

    @Test fun `createTask inserts optimistic row before network`() = runTest {
        coEvery { api.createTask(any()) } returns
                TaskDto("server-1", "t", "d", "high", "pending", "2026-05-23T12:30:00Z", null)

        val result = repo.createTask("t", "d", TaskPriority.HIGH, TaskStatus.PENDING)

        assertThat(result).isInstanceOf(AppResult.Success::class.java)
        coVerify(ordering = Ordering.ORDERED) {
            dao.upsert(match { it.syncState == SyncState.PENDING_CREATE.name }) // optimistic first
            api.createTask(any())
            dao.upsert(match { it.syncState == SyncState.SYNCED.name })          // reconciled
        }
    }

    @Test fun `createTask offline keeps optimistic row and still succeeds`() = runTest {
        coEvery { api.createTask(any()) } throws IOException()
        val result = repo.createTask("t", "d", TaskPriority.LOW, TaskStatus.PENDING)
        assertThat(result).isInstanceOf(AppResult.Success::class.java) // offline-first contract
        coVerify { dao.upsert(match { it.syncState == SyncState.PENDING_CREATE.name }) }
    }
}