package com.example.pulse_app.features.task.data.mappers

import com.example.pulse_app.core.enums.SyncState
import com.example.pulse_app.features.task.data.datasource.local.TaskEntity
import com.example.pulse_app.features.task.data.datasource.remote.dto.TaskDto
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