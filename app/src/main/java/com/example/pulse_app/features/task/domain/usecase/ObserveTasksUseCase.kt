package com.example.pulse_app.features.task.domain.usecase

import com.example.pulse_app.core.usecase.FlowUseCase
import com.example.pulse_app.core.usecase.NoParams
import com.example.pulse_app.features.task.domain.model.TaskModel
import com.example.pulse_app.features.task.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
//** Observer local database tasks */
class ObserveTasksUseCase @Inject constructor(private val repo: TaskRepository)
    : FlowUseCase<NoParams,List<TaskModel>> {
    override fun invoke(params: NoParams): Flow<List<TaskModel>>  = repo.observeTasks()
}