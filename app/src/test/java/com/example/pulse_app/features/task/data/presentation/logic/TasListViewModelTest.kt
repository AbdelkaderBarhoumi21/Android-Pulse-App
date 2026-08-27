package com.example.pulse_app.features.task.data.presentation.logic

import app.cash.turbine.test
import com.example.pulse_app.core.error.AppFailure
import com.example.pulse_app.core.result.AppResult
import com.example.pulse_app.features.task.domain.model.TaskModel
import com.example.pulse_app.features.task.domain.model.TaskPriority
import com.example.pulse_app.features.task.domain.model.TaskStatus
import com.example.pulse_app.features.task.domain.usecase.ObserveTasksUseCase
import com.example.pulse_app.features.task.domain.usecase.RefreshTasksUseCase
import com.example.pulse_app.features.task.presentation.logic.TaskListViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class TasListViewModelTest {
    private val observe = mockk<ObserveTasksUseCase>()
    private val refresh = mockk<RefreshTasksUseCase>()

    @Before fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `emits tasks from observe use case`() =
        runTest {
            // Arrange
            val task = TaskModel("1", "t", "d", TaskPriority.HIGH, TaskStatus.PENDING, Instant.now())

            every {
                observe(any())
            } returns flowOf(listOf(task))
            coEvery {
                refresh(any())
            } returns AppResult.Success(Unit)

            // Act
            val vm = TaskListViewModel(observe, refresh)
            vm.uiState.test {
                // skip initial empty, advance, assert populated state
                awaitItem()
                runCurrent()
                // Assert
                assertThat(expectMostRecentItem().tasks).containsExactly(task)
            }
        }

    @Test
    fun `refresh failure surfaces error messages`() =
        runTest {
            every { observe(any()) } returns flowOf(emptyList())
            coEvery { refresh(any()) } returns AppResult.Error(AppFailure.NetworkFailure)

            val vm = TaskListViewModel(observe, refresh)
            runCurrent()
            assertThat(vm.uiState.value.errorMessage).isEqualTo(AppFailure.NetworkFailure.message)
        }
}
