package com.praveen.snoretrack

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.praveen.snoretrack.data.AppDatabase
import com.praveen.snoretrack.data.Session
import com.praveen.snoretrack.data.SessionDao
import com.praveen.snoretrack.data.SnoreEvent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var database: AppDatabase
    private lateinit var sessionDao: SessionDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Use in-memory database for testing
        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()

        sessionDao = database.sessionDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testInsertAndRetrieveSession() = runBlocking {
        // Arrange
        val session = Session(
            startTime = System.currentTimeMillis(),
            endTime = System.currentTimeMillis() + 10000,
            snoreScore = 456.78f,
            fullAudioPath = "/test/path.pcm",
            isSnippet = false
        )

        // Act
        val sessionId = sessionDao.insertSession(session)
        val allSessions = sessionDao.getAllSessions().first()

        // Assert
        assertEquals(1, allSessions.size)
        assertEquals(sessionId, allSessions[0].id)
        assertEquals(session.snoreScore, allSessions[0].snoreScore)
        assertEquals(session.fullAudioPath, allSessions[0].fullAudioPath)
    }

    @Test
    fun testGetAllSessions() = runBlocking {
        // Arrange
        val session1 = Session(
            startTime = 1000L,
            endTime = 2000L,
            snoreScore = 100f,
            fullAudioPath = "/path1.pcm",
            isSnippet = false
        )
        val session2 = Session(
            startTime = 3000L,
            endTime = 4000L,
            snoreScore = 200f,
            fullAudioPath = "/path2.pcm",
            isSnippet = false
        )

        // Act
        sessionDao.insertSession(session1)
        sessionDao.insertSession(session2)
        val sessions = sessionDao.getAllSessions().first()

        // Assert
        assertEquals(2, sessions.size)
        // Sessions ordered by startTime DESC, so newest first
        assertEquals("/path2.pcm", sessions[0].fullAudioPath)
        assertEquals("/path1.pcm", sessions[1].fullAudioPath)
    }

    @Test
    fun testUpdateSession() = runBlocking {
        // Arrange
        val session = Session(
            startTime = 1000L,
            endTime = 2000L,
            snoreScore = 100f,
            fullAudioPath = "/path.pcm",
            isSnippet = false
        )
        val sessionId = sessionDao.insertSession(session)

        // Act
        val updatedSession = session.copy(
            id = sessionId,
            snoreScore = 999f,
            endTime = 5000L
        )
        sessionDao.updateSession(updatedSession)
        val allSessions = sessionDao.getAllSessions().first()

        // Assert
        assertEquals(1, allSessions.size)
        assertEquals(999f, allSessions[0].snoreScore)
        assertEquals(5000L, allSessions[0].endTime)
    }

    @Test
    fun testDeleteSession() = runBlocking {
        // Arrange
        val session = Session(
            startTime = 1000L,
            endTime = 2000L,
            snoreScore = 100f,
            fullAudioPath = "/path.pcm",
            isSnippet = false
        )
        val sessionId = sessionDao.insertSession(session)

        // Act
        sessionDao.deleteSession(session.copy(id = sessionId))
        val allSessions = sessionDao.getAllSessions().first()

        // Assert
        assertEquals(0, allSessions.size)
    }

    @Test
    fun testInsertAndRetrieveSnoreEvent() = runBlocking {
        // Arrange
        val session = Session(
            startTime = 1000L,
            endTime = 2000L,
            snoreScore = 100f,
            fullAudioPath = "/path.pcm",
            isSnippet = false
        )
        val sessionId = sessionDao.insertSession(session)

        val snoreEvent = SnoreEvent(
            sessionId = sessionId,
            timestamp = 1500L,
            amplitude = 600.0,
            frequency = 250.0
        )

        // Act
        sessionDao.insertEvent(snoreEvent)
        val events = sessionDao.getEventsForSession(sessionId).first()

        // Assert
        assertEquals(1, events.size)
        assertEquals(sessionId, events[0].sessionId)
        assertEquals(600.0, events[0].amplitude, 0.01)
        assertEquals(250.0, events[0].frequency, 0.01)
    }

    @Test
    fun testMultipleSnoreEventsForSession() = runBlocking {
        // Arrange
        val session = Session(
            startTime = 1000L,
            endTime = 5000L,
            snoreScore = 100f,
            fullAudioPath = "/path.pcm",
            isSnippet = false
        )
        val sessionId = sessionDao.insertSession(session)

        val events = listOf(
            SnoreEvent(sessionId = sessionId, timestamp = 1500L, amplitude = 600.0, frequency = 250.0),
            SnoreEvent(sessionId = sessionId, timestamp = 2500L, amplitude = 700.0, frequency = 280.0),
            SnoreEvent(sessionId = sessionId, timestamp = 3500L, amplitude = 650.0, frequency = 260.0)
        )

        // Act
        events.forEach { sessionDao.insertEvent(it) }
        val retrievedEvents = sessionDao.getEventsForSession(sessionId).first()

        // Assert
        assertEquals(3, retrievedEvents.size)
        assertTrue(retrievedEvents.all { it.sessionId == sessionId })
    }

    @Test
    fun testDeleteOldSessions() = runBlocking {
        // Arrange
        val oldTime = System.currentTimeMillis() - (8 * 24 * 60 * 60 * 1000L) // 8 days ago
        val recentTime = System.currentTimeMillis() - (1 * 24 * 60 * 60 * 1000L) // 1 day ago
        val cutoffTime = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L) // 7 days ago

        val oldSession = Session(
            startTime = oldTime,
            endTime = oldTime + 10000,
            snoreScore = 100f,
            fullAudioPath = "/old.pcm",
            isSnippet = false
        )
        val recentSession = Session(
            startTime = recentTime,
            endTime = recentTime + 10000,
            snoreScore = 200f,
            fullAudioPath = "/recent.pcm",
            isSnippet = false
        )

        sessionDao.insertSession(oldSession)
        sessionDao.insertSession(recentSession)

        // Act
        sessionDao.deleteOldSessions(cutoffTime)
        val remainingSessions = sessionDao.getAllSessions().first()

        // Assert
        assertEquals(1, remainingSessions.size)
        assertEquals("/recent.pcm", remainingSessions[0].fullAudioPath)
    }

    @Test
    fun testGetAllSessionsSuspend() = runBlocking {
        // Arrange
        val session1 = Session(
            startTime = 1000L,
            endTime = 2000L,
            snoreScore = 100f,
            fullAudioPath = "/path1.pcm",
            isSnippet = false
        )
        val session2 = Session(
            startTime = 3000L,
            endTime = 4000L,
            snoreScore = 200f,
            fullAudioPath = "/path2.pcm",
            isSnippet = false
        )

        // Act
        sessionDao.insertSession(session1)
        sessionDao.insertSession(session2)
        val sessions = sessionDao.getAllSessionsSuspend()

        // Assert
        assertEquals(2, sessions.size)
    }

    @Test
    fun testDatabaseTransaction() = runBlocking {
        // Arrange
        val session = Session(
            startTime = 1000L,
            endTime = 5000L,
            snoreScore = 100f,
            fullAudioPath = "/path.pcm",
            isSnippet = false
        )

        // Act: Insert session and events
        val sessionId = sessionDao.insertSession(session)
        val event1 = SnoreEvent(sessionId = sessionId, timestamp = 1500L, amplitude = 600.0, frequency = 250.0)
        val event2 = SnoreEvent(sessionId = sessionId, timestamp = 2500L, amplitude = 700.0, frequency = 280.0)

        sessionDao.insertEvent(event1)
        sessionDao.insertEvent(event2)

        // Assert: Verify both session and events exist
        val allSessions = sessionDao.getAllSessions().first()
        val retrievedEvents = sessionDao.getEventsForSession(sessionId).first()

        assertEquals(1, allSessions.size)
        assertEquals(2, retrievedEvents.size)
    }
}
