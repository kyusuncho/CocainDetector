package com.example.cocainedetector.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Detection::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun detectionDao(): DetectionDao
}
