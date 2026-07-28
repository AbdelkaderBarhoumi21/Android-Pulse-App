package com.example.pulse_app.features.task.data.mapper

import com.example.pulse_app.core.enums.SyncState
import com.example.pulse_app.features.task.data.datasource.local.TaskEntity
import com.example.pulse_app.features.task.data.datasource.remote.dto.TaskDto
import com.example.pulse_app.features.task.domain.model.TaskModel
import com.example.pulse_app.features.task.domain.model.TaskPriority
import com.example.pulse_app.features.task.domain.model.TaskStatus
import java.time.Instant

// DTO -> Entity (incoming server truth is SYNCED)
fun TaskDto.toEntity(): TaskEntity = TaskEntity(
    id= id,
    title= title,
    description= description,
    priority= priority,
    status=status,
    createdAt=Instant.parse(createdAt),
    completedAt = completedAt?.let(Instant::parse),
    syncState = SyncState.SYNCED.name,
)

// Entity -> domain
fun TaskEntity.toDomain() = TaskModel(
    id = id,
    title= title,
    description=description,
    priority = TaskPriority.fromApi(priority),
    status = TaskStatus.fromApi(status),
    createdAt = createdAt,
    completedAt = completedAt

)

// Domain -> Entity (with explicit sync state)
fun TaskModel.toEntity()= TaskEntity(
    id = id,
    title = title,
    description = description,
    priority = priority.wire,
    status = status.wire,
    createdAt = createdAt,
    completedAt = completedAt
)