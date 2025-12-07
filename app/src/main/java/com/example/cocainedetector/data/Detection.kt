package com.example.cocainedetector.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detections")
data class Detection(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val packageName: String,
    val screenText: String,
    val detectedText: String
)
