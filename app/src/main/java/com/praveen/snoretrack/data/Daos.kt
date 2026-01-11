package com.praveen.snoretrack.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Insert
    suspend fun insertSession(session: Session): Long

    @androidx.room.Update
    suspend fun updateSession(session: Session)

    @Query("SELECT * FROM sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<Session>>

    @Query("SELECT * FROM sessions ORDER BY startTime DESC")
    suspend fun getAllSessionsSuspend(): List<Session>

    @Insert
    suspend fun insertEvent(event: SnoreEvent)

    @Query("SELECT * FROM snore_events WHERE sessionId = :sessionId")
    fun getEventsForSession(sessionId: Long): Flow<List<SnoreEvent>>
    
    // Cleanup helper
    @Query("DELETE FROM sessions WHERE startTime < :threshold")
    suspend fun deleteOldSessions(threshold: Long)
    
    @androidx.room.Delete
    suspend fun deleteSession(session: Session)
}
