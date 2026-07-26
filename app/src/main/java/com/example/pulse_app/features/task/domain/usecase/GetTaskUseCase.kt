package com.example.pulse_app.features.task.domain.usecase

import com.example.pulse_app.core.usecase.FlowUseCase
import com.example.pulse_app.features.task.domain.model.TaskModel
import com.example.pulse_app.features.task.domain.repository.TaskRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetTaskUseCase @Inject constructor(private val repo: TaskRepository): FlowUseCase<String, TaskModel?> {
    override fun invoke(id: String): Flow<TaskModel?> = repo.observeTask(id)
}