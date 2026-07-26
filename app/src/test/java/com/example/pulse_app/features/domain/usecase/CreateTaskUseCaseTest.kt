package com.example.pulse_app.features.domain.usecase
import com.example.pulse_app.core.error.AppFailure
import com.example.pulse_app.core.result.AppResult
import com.example.pulse_app.core.usecase.UseCase
import com.example.pulse_app.features.task.domain.model.TaskModel
import com.google.common.truth.Truth.assertThat
import com.example.pulse_app.features.task.domain.model.TaskPriority
import com.example.pulse_app.features.task.domain.model.TaskStatus
import com.example.pulse_app.features.task.domain.repository.TaskRepository
import com.example.pulse_app.features.task.domain.usecase.CreateTaskParams
import com.example.pulse_app.features.task.domain.usecase.CreateTaskUseCase
import io.mockk.mockk
import io.mockk.coVerify
import io.mockk.coEvery
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.Instant

class CreateTaskUseCaseTest{
    private val repo = mockk<TaskRepository> ()
    private val useCase= CreateTaskUseCase(repo)

    @Test
    fun `blank title fails validation without hitting repo`()= runTest {
        val result = useCase(CreateTaskParams(" ", "d", TaskPriority.LOW))
        assertThat(result).isInstanceOf(AppResult.Error::class.java)
        assertThat((result as AppResult.Error ) .failure).isInstanceOf(AppFailure.ValidationFailure::class.java)
        coVerify  (exactly = 0) { repo.createTask(any(),any(),any(),any())}
    }
    @Test
    fun `valid input trims and delegates to repo`()= runTest{
        val task= TaskModel("1", "Milk", "2L", TaskPriority.LOW, TaskStatus.PENDING, Instant.now())
        coEvery { repo.createTask("Buy milk","2L", TaskPriority.LOW, TaskStatus.PENDING) } returns AppResult.Success(task)
        val result = useCase(CreateTaskParams(" Buy milk ", " 2L ", TaskPriority.HIGH))
        assertThat((result as AppResult.Success).data).isEqualTo(task)
        coVerify { repo.createTask("Buy milk", "2L", TaskPriority.HIGH, TaskStatus.PENDING) }
    }
}