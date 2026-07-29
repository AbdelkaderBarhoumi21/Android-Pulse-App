package com.example.pulse_app.features.task.data.repository

import com.example.pulse_app.core.di.IoDispatcher
import com.example.pulse_app.core.enums.SyncState
import com.example.pulse_app.core.error.AppErrorMapper
import com.example.pulse_app.core.error.AppException
import com.example.pulse_app.core.network.safeApiCall
import com.example.pulse_app.core.result.AppResult
import com.example.pulse_app.features.task.data.datasource.local.TaskDao
import com.example.pulse_app.features.task.data.datasource.remote.TaskApi
import com.example.pulse_app.features.task.data.datasource.remote.dto.CreateTaskRequestDto
import com.example.pulse_app.features.task.data.datasource.remote.dto.UpdateTaskRequestDto
import com.example.pulse_app.features.task.data.mapper.toDomain
import com.example.pulse_app.features.task.data.mapper.toEntity
import com.example.pulse_app.features.task.domain.model.TaskModel
import com.example.pulse_app.features.task.domain.model.TaskPriority
import com.example.pulse_app.features.task.domain.model.TaskStatus
import com.example.pulse_app.features.task.domain.repository.TaskRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject
import java.util.UUID

class TaskRepositoryImpl @Inject constructor(
    private val api: TaskApi,
    private val dao: TaskDao,
    @IoDispatcher private val io: CoroutineDispatcher
) : TaskRepository{

    // Observe tasks from local DB
    override fun observeTasks(): Flow<List<TaskModel>> =
        dao.observeTasks().map { list -> list.map { it.toDomain() } }
    // Observe task by id from local DB
    override fun observeTask(id: String): Flow<TaskModel?>  =
        dao.observeTask(id).map { task -> task?.toDomain() }

    // Refresh task => // Keep local pending rows; upsert the rest from server.
    override suspend fun refreshTasks(): AppResult<Unit> = withContext(io){
        runResult{
            val remote = safeApiCall { api.getTasks() }
            dao.replaceAllKeepingPending(remote.map { tasksDto->tasksDto.toEntity() })
        }
    }

    // create task locally and send api call
    override suspend fun createTask(
        title: String,
        description: String,
        priority: TaskPriority,
        status: TaskStatus
    ): AppResult<TaskModel> = withContext(io){
        // 1) optimistic local insert with a client-generated id
        val local = TaskModel(
            id= UUID.randomUUID().toString(),
            title = title,
            description = description,
            priority = priority,
            status = status,
            createdAt = Instant.now(),
            completedAt = null,
        )
        dao.upsert(local.toEntity(SyncState.PENDING_CREATE))
        // 2) try to push immediately; if offline, the worker retries later
        runResult {
            val dto = safeApiCall {  api.createTask(CreateTaskRequestDto(
                title,description,priority.wire,status.wire
            ))
            }
            // server id may differ from local id -> swap rows
            dao.hardDelete(local.id)
            dao.upsert(dto.toEntity())
            dto.toEntity().toDomain()
        }.let {
            // even on failure we keep the optimistic row; surface success with local copy
            result -> if (result is AppResult.Error) AppResult.Success(local) else result
        }
    }

    // Update task locally and send api call
    override suspend fun updateTask(
        id: String,
        title: String?,
        description: String?,
        priority: TaskPriority?,
        status: TaskStatus?
    ): AppResult<TaskModel> = withContext(io){
        val current= dao.getTask(id) ?: return@withContext AppResult.Error(AppErrorMapper.map(
            AppException.NotFoundException("Task")
        )
        )

        val merged= current.copy(
            title = title ?: current.title,
            description = description ?: current.description,
            priority = priority?.wire ?: current.priority,
            status =status?.wire ?: current.status,
            completedAt = if(status== TaskStatus.COMPLETED) Instant.now() else current.completedAt,
            syncState = SyncState.PENDING_UPDATE.name,
            updatedAtLocal = Instant.now()
         )
        dao.upsert(merged)
        runResult {
            val dto=safeApiCall{
                api.updateTask(id=id,body= UpdateTaskRequestDto(
                    title,description,priority?.wire,status?.wire)
                )
            }
            dao.upsert(dto.toEntity()) // syncState = SYNCED
            dto.toEntity().toDomain()
        }.let { result ->
            if(result is AppResult.Error) AppResult.Success(merged.toDomain()) else result
        }
    }

    override suspend fun deleteTask(id: String): AppResult<Unit> = withContext(io){
        dao.markSyncState(id, SyncState.PENDING_DELETE.name)  // soft delete, hidden from UI
        runResult {
            safeApiCall { api.deleteTask(id) }
            dao.hardDelete(id)
        }.let {
            result -> if(result is AppResult.Error) AppResult.Success(Unit) else result
        }
    }

    override suspend fun syncPending(): AppResult<Unit> = withContext(io){
        runResult {
            dao.getPending().forEach { e -> when(SyncState.valueOf(e.syncState))
            {
                SyncState.PENDING_CREATE -> {
                    val dto = safeApiCall {
                        api.createTask(
                            CreateTaskRequestDto( e.title,e.description,e.priority,e.status)
                        )
                    }
                    dao.hardDelete(e.id)
                    dao.upsert(e)
                }

                SyncState.PENDING_UPDATE -> {
                    val dto = safeApiCall {
                        api.updateTask(id = e.id, body= UpdateTaskRequestDto(e.title,e.description,e.priority,e.status,))
                    }
                    dao.upsert(dto.toEntity())
                }

                SyncState.PENDING_DELETE -> {
                    safeApiCall { api.deleteTask(e.id) }
                    dao.hardDelete(e.id)
                }

                SyncState.SYNCED -> Unit


            }
            }
        }
    }




    /** Local helper: run a block, convert thrown AppException → AppResult.Error. */
    private inline fun <T> runResult(block:()-> T): AppResult<T> = try {
        AppResult.Success(block())
    }catch (e: Throwable){
        AppResult.Error(AppErrorMapper.map(e))
    }
}