package com.example.pulse_app.features.task.domain.usecase

import com.example.pulse_app.core.usecase.NoParams
import com.example.pulse_app.core.usecase.UseCase
import com.example.pulse_app.features.task.domain.repository.TaskRepository
import javax.inject.Inject

class RefreshTasksUseCase @Inject constructor(private val repo: TaskRepository) :
    UseCase<NoParams, Unit> {
    override suspend fun invoke(params: NoParams) = repo.refreshTasks()
}