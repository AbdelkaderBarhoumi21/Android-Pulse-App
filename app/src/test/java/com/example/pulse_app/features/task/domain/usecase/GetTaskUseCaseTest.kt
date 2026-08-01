package com.example.pulse_app.features.task.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.example.pulse_app.features.task.domain.model.TaskModel
import com.example.pulse_app.features.task.domain.model.TaskPriority
import com.example.pulse_app.features.task.domain.model.TaskStatus
import com.example.pulse_app.features.task.domain.repository.TaskRepository
import com.example.pulse_app.features.task.domain.usecase.GetTaskUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.Instant

class GetTaskUseCaseTest {
    private val repo = mockk<TaskRepository>()
    private val useCase= GetTaskUseCase(repo)
    @Test fun `emits task from repo observation`() = runTest {
        // ── ARRANGE ──
        val task = TaskModel("42", "Buy milk", "2L", TaskPriority.HIGH, TaskStatus.COMPLETED, Instant.now())
        every { repo.observeTask("42") } returns flowOf(task)

        // ── ACT ──
        val emissions = useCase("42").toList()

        // ── ASSERT ──
        assertThat(emissions).containsExactly(task)
        verify { repo.observeTask("42") }
    }

    @Test
    fun `emits null when repo has no task for this id`()= runTest {
        // ── ARRANGE ──
        every { repo.observeTask("9") } returns flowOf(null)

        // ── ACT ──
        val emission = useCase("9").toList()

        // ── ASSERT ──
        assertThat(emission).containsExactly( null)

        verify { repo.observeTask("9") }
    }

}