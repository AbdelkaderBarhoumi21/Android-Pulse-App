package com.example.pulse_app.features.task.domain.usecase
import com.example.pulse_app.core.error.AppFailure
import com.example.pulse_app.core.result.AppResult
import com.example.pulse_app.core.usecase.UseCase
import com.example.pulse_app.features.task.domain.model.TaskModel
import com.example.pulse_app.features.task.domain.model.TaskPriority
import com.example.pulse_app.features.task.domain.model.TaskStatus
import com.example.pulse_app.features.task.domain.repository.TaskRepository
import javax.inject.Inject
data class UpdateTaskParams(
    val id: String, val title: String? = null, val description: String? = null,
    val priority: TaskPriority? = null, val status: TaskStatus? = null,
)
class UpdateTaskUseCase @Inject constructor(private val repo: TaskRepository) :
    UseCase<UpdateTaskParams, TaskModel> {

    override suspend fun invoke(params: UpdateTaskParams) : AppResult<TaskModel> {
        if(params.title != null && params.title.isBlank()){
            return AppResult.Error(AppFailure.ValidationFailure("Title cannot be blank"))
        }
        return repo.updateTask(params.id, params.title, params.description, params.priority, params.status)

    }
}