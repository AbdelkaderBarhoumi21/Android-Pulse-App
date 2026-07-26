package com.example.pulse_app.features.task.domain.repository
import com.example.pulse_app.core.result.AppResult
import com.example.pulse_app.features.task.domain.model.TaskModel
import com.example.pulse_app.features.task.domain.model.TaskPriority
import com.example.pulse_app.features.task.domain.model.TaskStatus
import kotlinx.coroutines.flow.Flow
interface TaskRepository{
    //** UI observes Room(offline-first source of truth) */
    fun observeTasks(): Flow<List<TaskModel>>
    fun observeTask(id: String):Flow<TaskModel?>

    /** Pull remote → reconcile Room. */
    suspend fun refreshTasks(): AppResult<Unit>

    /** Optimistic local write + queued remote push (see sync engine). */
    suspend fun createTask(title:String,description:String
                           ,priority: TaskPriority
                           ,status: TaskStatus= TaskStatus.PENDING): AppResult<TaskModel>
    suspend fun updateTask(id: String, title: String? = null, description: String? = null,
                           priority: TaskPriority? = null, status: TaskStatus? = null): AppResult<TaskModel>
    suspend fun deleteTask(id:String): AppResult<Unit>
    /** Used by the sync worker to push pending local changes. */
    suspend fun syncPending(): AppResult<Unit>




}