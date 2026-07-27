package com.example.pulse_app.features.domain.usecase
import androidx.room.Update
import com.example.pulse_app.core.error.AppFailure
import com.example.pulse_app.core.result.AppResult
import com.example.pulse_app.features.task.domain.model.TaskModel
import com.example.pulse_app.features.task.domain.model.TaskPriority
import com.example.pulse_app.features.task.domain.model.TaskStatus
import com.example.pulse_app.features.task.domain.repository.TaskRepository
import com.example.pulse_app.features.task.domain.usecase.UpdateTaskParams
import com.example.pulse_app.features.task.domain.usecase.UpdateTaskUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.Instant
import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify

class UpdateTaskUseCaseTest{
    private val repo = mockk<TaskRepository>()
    private val useCase= UpdateTaskUseCase(repo)

    @Test fun `delegates partial update straight to repo`() = runTest {
        // ── ARRANGE ──
        val task = TaskModel("42", "Buy milk", "2L", TaskPriority.HIGH, TaskStatus.COMPLETED, Instant.now())
        coEvery { repo.updateTask("42", null, null, null, TaskStatus.COMPLETED) } returns
                AppResult.Success(task)

        // ── ACT ──
        val result = useCase(UpdateTaskParams(id = "42", status = TaskStatus.COMPLETED))

        // ── ASSERT ──
        assertThat((result as AppResult.Success).data).isEqualTo(task)
        coVerify { repo.updateTask("42", null, null, null, TaskStatus.COMPLETED) }
    }

    @Test
    fun `returns AppFailure when title is blank, without calling repo`() = runTest {
        // --Arrange--
        // no coEvery needed - repo should never be called in this path

        // --Act--
        val result = useCase(UpdateTaskParams("42", "  "))

        assertThat(result).isInstanceOf(AppResult.Error::class.java)
        assertThat((result as AppResult.Error).failure).isInstanceOf(AppFailure.ValidationFailure::class.java)
        coVerify (exactly = 0){ repo.updateTask(any(),any(),any(),any(),any())  }
    }

    @Test
    fun `passes blank id straight to repo`()= runTest {
        // ── ARRANGE ──
        coEvery {
            repo.updateTask("",  null,null,null, TaskStatus.COMPLETED)
        } returns AppResult.Error(
            AppFailure.NotFoundFailure) // whatever your repo would realistically return

        // ── ACT ──
        val result = useCase(UpdateTaskParams("", status = TaskStatus.COMPLETED))
        // ── ASSERT ──
        assertThat(result).isInstanceOf(AppResult.Error::class.java)
        coVerify { repo.updateTask("",null,null,null, TaskStatus.COMPLETED) }

    }

}