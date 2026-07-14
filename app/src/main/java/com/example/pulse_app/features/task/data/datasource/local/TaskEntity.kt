package com.example.pulse_app.features.task.data.datasource.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pulse_app.core.enums.SyncState
import java.time.Instant

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description:String,
    val priority:String,
    val status:String,
    val createdAt: Instant,
    val completedAt:Instant,
    val syncState: String = SyncState.SYNCED.name,
    val updatedAtLocal:Instant =Instant.now()
)