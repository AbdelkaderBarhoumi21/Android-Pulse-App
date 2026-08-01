package com.example.pulse_app.features.task.domain.usecase

import com.example.pulse_app.core.error.AppFailure
import com.example.pulse_app.core.result.AppResult
import com.example.pulse_app.core.usecase.UseCase
import com.example.pulse_app.features.task.domain.model.TaskModel
import com.example.pulse_app.features.task.domain.model.TaskPriority
import com.example.pulse_app.features.task.domain.model.TaskStatus
import com.example.pulse_app.features.task.domain.repository.TaskRepository
import javax.inject.Inject

data class CreateTaskParams(
    val title:String,
    val description:String,
    val priority: TaskPriority,
    val status: TaskStatus = TaskStatus.PENDING
)

class CreateTaskUseCase @Inject constructor(private val repo: TaskRepository) : UseCase<CreateTaskParams, TaskModel> {
    override suspend fun invoke(params: CreateTaskParams): AppResult<TaskModel> {
        if (params.title.isBlank()){
            return AppResult.Error(AppFailure.ValidationFailure("Title is required"))
        }
        return repo.createTask(params.title.trim(),params.description.trim(),params.priority,params.status)
    }
}