package com.example.pulse_app.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.pulse_app.features.task.data.datasource.local.TaskDao
import com.example.pulse_app.features.task.data.datasource.local.TaskEntity

@Database(entities = [TaskEntity::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase(){
    abstract fun taskDao(): TaskDao
}