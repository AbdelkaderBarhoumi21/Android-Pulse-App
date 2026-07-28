package com.example.pulse_app.features.task.domain.usecase

/*
                            Manual (launch)	                                                   Turbine (.test { })
Who starts the collector	You, explicitly, with launch { }	                               Turbine, automatically, inside .test { }
How you read values	        Push into a MutableList, assert on the whole list at the end	   Pull one at a time with awaitItem(), assert immediately
 */
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.example.pulse_app.core.usecase.NoParams
import com.example.pulse_app.features.task.domain.model.TaskModel
import com.example.pulse_app.features.task.domain.model.TaskPriority
import com.example.pulse_app.features.task.domain.model.TaskStatus
import com.example.pulse_app.features.task.domain.repository.TaskRepository
import com.example.pulse_app.features.task.domain.usecase.ObserveTasksUseCase
import org.junit.Test
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import io.mockk.mockk
import io.mockk.every
import io.mockk.verify
import okhttp3.internal.concurrent.Task
import java.time.Instant

class ObserveTasksUseCaseTest{

    private val repo = mockk<TaskRepository>()
    private val useCase= ObserveTasksUseCase(repo)

    @Test
    fun `emits updated task list as repo pushes new values`()= runTest {
        // ── ARRANGE ──
        val fakeDbFlow= MutableSharedFlow<List<TaskModel>>(replay = 1)
        every {
            repo.observeTasks()
        } returns fakeDbFlow

        val taskA = TaskModel("1", "Buy milk", "2L", TaskPriority.HIGH, TaskStatus.PENDING, Instant.now())
        val taskB = TaskModel("2", "Walk dog", "EB", TaskPriority.LOW, TaskStatus.PENDING, Instant.now())

        // ── ACT + ASSERT ──
        useCase(NoParams).test {

            // Emit empty list
            fakeDbFlow.emit(emptyList())
            assertThat(awaitItem()).isEmpty()

            // Emit taskA
            fakeDbFlow.emit(listOf(taskA))
            assertThat(awaitItem()).containsExactly(taskA)

            // Emit taskA + taskB
            fakeDbFlow.emit(listOf(taskA,taskB))
            assertThat(awaitItem()).containsExactly(taskA,taskB)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents() // real Room flow never completes on its own


        }

    }

    @Test
    fun `emits empty list when there are ni tasks`() = runTest {
        // ── ARRANGE ──
        every {
            repo.observeTasks()
        } returns MutableSharedFlow<List<TaskModel>>(replay = 1).apply { tryEmit(emptyList()) }

        // ── ACT + ASSERT ──

        useCase(NoParams).test {
            assertThat(awaitItem()).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `propagates error thrown by repo`() = runTest {
        // ── ARRANGE ──
        val boom = RuntimeException("DB read failed")
        every {
            repo.observeTasks()
        } returns flow {
            emit(emptyList())
            throw boom
        }

        // ── ACT + ASSERT ──

        useCase(NoParams).test {
            assertThat(awaitItem()).isEmpty()
            assertThat(awaitError()).isEqualTo(boom)
        }

    }

}