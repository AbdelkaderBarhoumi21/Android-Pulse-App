package com.example.pulse_app.features.task.domain.usecase

import com.example.pulse_app.core.error.AppFailure
import com.example.pulse_app.core.result.AppResult
import com.example.pulse_app.core.usecase.UseCase
import com.example.pulse_app.features.task.domain.model.TaskModel
import com.example.pulse_app.features.task.domain.model.TaskPriority
import com.example.pulse_app.features.task.domain.model.TaskStatus
import com.example.pulse_app.features.task.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(private val repo: TaskRepository) :
    UseCase<String, Unit> {
    override suspend fun invoke(params: String) = repo.deleteTask(params)
}