package com.praveen.snoretrack.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startTime: Long,
    val endTime: Long,
    val snoreScore: Float, // Aggregate score
    val fullAudioPath: String,
    val isSnippet: Boolean = false // If true, this might be a standalone snippet? Or separate table? Separate table better.
)

@Entity(tableName = "snore_events")
data class SnoreEvent(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: Long,
    val timestamp: Long,
    val amplitude: Double,
    val frequency: Double
)
