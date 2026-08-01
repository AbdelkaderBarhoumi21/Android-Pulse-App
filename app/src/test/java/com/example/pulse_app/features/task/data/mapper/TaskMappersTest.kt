package com.example.pulse_app.features.task.data.mapper

import com.example.pulse_app.core.enums.SyncState
import com.example.pulse_app.features.task.data.datasource.remote.dto.TaskDto
import com.example.pulse_app.features.task.domain.model.TaskPriority
import com.example.pulse_app.features.task.domain.model.TaskStatus
import com.google.common.truth.Truth.assertThat
import okhttp3.internal.concurrent.Task
import org.junit.Test

class TaskMappersTest{
    @Test
    fun `dto maps to synced entity then to domain`(){
        val dto = TaskDto(
            id = "1",
            title= "Buy milk",
            description = "2L",
            priority = "high",
            status = "inProgress",
            createdAt = "2026-05-23T12:30:00Z",
            completedAt = null
        )

        val domain = dto.toEntity().toDomain()
        assertThat(domain.id).isEqualTo("1")
        assertThat(domain.priority).isEqualTo(TaskPriority.HIGH)
        assertThat(domain.status).isEqualTo(TaskStatus.IN_PROGRESS)
        assertThat(dto.toEntity().syncState).isEqualTo(SyncState.SYNCED.name)
    }

    @Test
    fun `unknow enum values fall back to safe defaults`(){
        val dto = TaskDto("1", "t", "d", "weird", "alien", "2026-05-23T12:30:00Z", null)
        val domain = dto.toEntity().toDomain()
        assertThat(domain.priority).isEqualTo(TaskPriority.LOW)
        assertThat(domain.status).isEqualTo(TaskStatus.PENDING)
    }
}