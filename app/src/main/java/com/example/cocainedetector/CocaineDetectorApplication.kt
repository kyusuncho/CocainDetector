package com.example.cocainedetector

import android.app.Application
import androidx.room.Room
import com.example.cocainedetector.data.AppDatabase

class CocaineDetectorApplication : Application() {
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "cocaine-detector-db"
        ).fallbackToDestructiveMigration()
            .build()
    }
}
