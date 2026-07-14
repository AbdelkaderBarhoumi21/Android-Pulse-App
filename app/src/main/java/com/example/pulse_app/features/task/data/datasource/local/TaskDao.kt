package com.example.pulse_app.features.task.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.pulse_app.core.enums.SyncState
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    /** Excludes soft-deleted rows. Ordered newest first (API contract). */
    @Query("SELECT * FROM tasks WHERE syncState != :deleted ORDER BY createdAt DESC")
    fun observeTasks(deleted: String = SyncState.PENDING_DELETE.name): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    fun observeTask(id: String): Flow<TaskEntity?>

    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    suspend fun getTask(id: String): TaskEntity?

    @Query("SELECT * FROM tasks WHERE syncState != :synced")
    suspend fun getPending(synced: String = SyncState.SYNCED.name): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(tasks: List<TaskEntity>)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun hardDelete(id: String)

    @Query("UPDATE tasks SET syncState = :state, updatedAtLocal = :now WHERE id = :id")
    suspend fun markSyncState(id: String, state: String, now: java.time.Instant = java.time.Instant.now())

    /** Replace whole table with server truth (used after a full refresh). */
    @Transaction
    suspend fun replaceAllKeepingPending(serverTasks:List<TaskEntity>){
        val pendingIds= getPending().map { task -> task.id  }.toSet()
        // Keep local pending rows; upsert the rest from server.
        upsertAll(serverTasks.filterNot { task-> task.id in pendingIds })
    }

}