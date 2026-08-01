package com.example.pulse_app.features.task.data.datasource.remote

import androidx.room.Delete
import com.example.pulse_app.core.network.ApiEndpoints
import com.example.pulse_app.features.task.data.datasource.remote.dto.CreateTaskRequestDto
import com.example.pulse_app.features.task.data.datasource.remote.dto.TaskDto
import com.example.pulse_app.features.task.data.datasource.remote.dto.UpdateTaskRequestDto
import retrofit2.http.*

interface TaskApi{
    /// Get all tasks from server.
    @GET(ApiEndpoints.TASKS)
    suspend fun getTasks():List<TaskDto>

    /// Get task from server
    @GET(ApiEndpoints.TASK_BY_ID)
    suspend fun getTask(@Path(ApiEndpoints.PATH_ID) id: String): TaskDto

    /// Create new task
    @POST(ApiEndpoints.TASKS)
    suspend fun createTask(@Body body: CreateTaskRequestDto): TaskDto

    /// Update task
    @PUT(ApiEndpoints.TASK_BY_ID)
    suspend fun updateTask(@Path(ApiEndpoints.PATH_ID) id :String,@Body body: UpdateTaskRequestDto): TaskDto

    /// Delete task
    @DELETE(ApiEndpoints.TASK_BY_ID)
    suspend fun deleteTask(@Path(ApiEndpoints.PATH_ID) id: String)
}