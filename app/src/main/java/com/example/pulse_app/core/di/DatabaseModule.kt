package com.example.pulse_app.core.di

import android.content.Context
import androidx.room.Room
import com.example.pulse_app.core.database.AppDatabase
import com.example.pulse_app.core.utils.AppConstants
import com.example.pulse_app.features.task.data.datasource.local.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun database(
        @ApplicationContext context: Context,
    ): AppDatabase =
        Room
            .databaseBuilder(
                context,
                AppDatabase::class.java,
                AppConstants.DATABASE_NAME,
            ).fallbackToDestructiveMigration(false)
            .build()

    @Provides
    @Singleton
    fun taskDao(db: AppDatabase): TaskDao = db.taskDao()
}
