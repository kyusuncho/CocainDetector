package com.example.cocainedetector.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DetectionDao {
    @Query("SELECT * FROM detections ORDER BY timestamp DESC")
    fun getAll(): Flow<List<Detection>>

    @Insert
    suspend fun insert(detection: Detection)
    
    @Query("DELETE FROM detections")
    suspend fun deleteAll()
}
