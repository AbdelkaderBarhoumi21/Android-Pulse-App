package com.example.pulse_app.features.task.data.datasource.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.pulse_app.core.database.AppDatabase
import com.example.pulse_app.core.enums.SyncState
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant

@RunWith(AndroidJUnit4::class)
class TaskDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var dao: TaskDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.taskDao()
    }

    @After
    fun teardown() = db.close()

    @Test
    fun upsert_then_observe_returns_row() = runTest {
        val e = TaskEntity("1", "t", "d", "high", "pending", Instant.now(), null)
        dao.upsert(e)
        dao.observeTasks().test {
            assertThat(awaitItem().map { it.id }).containsExactly("1")
        }
    }

    @Test
    fun soft_deleted_rows_are_hidden() = runTest {
        dao.upsert(TaskEntity("1", "t", "d", "low", "pending", Instant.now(), null))
        dao.markSyncState("1", SyncState.PENDING_DELETE.name)
        dao.observeTasks().test {
            assertThat(awaitItem()).isEmpty()
        }
    }

    @Test
    fun getPending_returns_unsynced_rows_only() = runTest {
        dao.upsert(
            TaskEntity(
                "1", "a", "", "low", "pending", Instant.now(), null,
                syncState = SyncState.PENDING_CREATE.name
            )
        )
        dao.upsert(
            TaskEntity(
                "2", "b", "", "low", "pending", Instant.now(), null,
                syncState = SyncState.SYNCED.name
            )
        )
        assertThat(dao.getPending().map { it.id }).containsExactly("1")
    }
}
