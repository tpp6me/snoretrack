package com.praveen.snoretrack.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EntitiesTest {

    @Test
    fun `test Session entity creation`() {
        // Arrange & Act
        val session = Session(
            id = 1L,
            startTime = 1000L,
            endTime = 2000L,
            snoreScore = 123.45f,
            fullAudioPath = "/path/to/audio.pcm",
            isSnippet = false
        )

        // Assert
        assertEquals(1L, session.id)
        assertEquals(1000L, session.startTime)
        assertEquals(2000L, session.endTime)
        assertEquals(123.45f, session.snoreScore, 0.01f)
        assertEquals("/path/to/audio.pcm", session.fullAudioPath)
        assertFalse(session.isSnippet)
    }

    @Test
    fun `test Session duration calculation`() {
        // Arrange
        val startTime = 1000L
        val endTime = 5000L
        val session = Session(
            id = 1L,
            startTime = startTime,
            endTime = endTime,
            snoreScore = 0f,
            fullAudioPath = "",
            isSnippet = false
        )

        // Act
        val duration = session.endTime - session.startTime

        // Assert
        assertEquals(4000L, duration)
    }

    @Test
    fun `test Session with zero snore score`() {
        // Arrange & Act
        val session = Session(
            id = 1L,
            startTime = 1000L,
            endTime = 2000L,
            snoreScore = 0f,
            fullAudioPath = "/path/to/audio.pcm",
            isSnippet = false
        )

        // Assert
        assertEquals(0f, session.snoreScore, 0.01f)
    }

    @Test
    fun `test Session with high snore score`() {
        // Arrange & Act
        val session = Session(
            id = 1L,
            startTime = 1000L,
            endTime = 2000L,
            snoreScore = 9999.99f,
            fullAudioPath = "/path/to/audio.pcm",
            isSnippet = false
        )

        // Assert
        assertTrue(session.snoreScore > 1000f)
    }

    @Test
    fun `test Session equality`() {
        // Arrange
        val session1 = Session(
            id = 1L,
            startTime = 1000L,
            endTime = 2000L,
            snoreScore = 123.45f,
            fullAudioPath = "/path/to/audio.pcm",
            isSnippet = false
        )
        val session2 = Session(
            id = 1L,
            startTime = 1000L,
            endTime = 2000L,
            snoreScore = 123.45f,
            fullAudioPath = "/path/to/audio.pcm",
            isSnippet = false
        )
        val session3 = Session(
            id = 2L,
            startTime = 1000L,
            endTime = 2000L,
            snoreScore = 123.45f,
            fullAudioPath = "/path/to/audio.pcm",
            isSnippet = false
        )

        // Assert
        assertEquals(session1, session2)
        assertNotEquals(session1, session3)
    }

    @Test
    fun `test SnoreEvent entity creation`() {
        // Arrange & Act
        val snoreEvent = SnoreEvent(
            id = 1L,
            sessionId = 100L,
            timestamp = 5000L,
            amplitude = 678.9,
            frequency = 234.5
        )

        // Assert
        assertEquals(1L, snoreEvent.id)
        assertEquals(100L, snoreEvent.sessionId)
        assertEquals(5000L, snoreEvent.timestamp)
        assertEquals(678.9, snoreEvent.amplitude, 0.01)
        assertEquals(234.5, snoreEvent.frequency, 0.01)
    }

    @Test
    fun `test SnoreEvent with low frequency`() {
        // Arrange & Act
        val snoreEvent = SnoreEvent(
            id = 1L,
            sessionId = 100L,
            timestamp = 5000L,
            amplitude = 500.0,
            frequency = 50.0 // Typical low snore frequency
        )

        // Assert
        assertTrue(snoreEvent.frequency < 600.0) // Within snore range
        assertTrue(snoreEvent.amplitude > 400.0)
    }

    @Test
    fun `test SnoreEvent with high amplitude`() {
        // Arrange & Act
        val snoreEvent = SnoreEvent(
            id = 1L,
            sessionId = 100L,
            timestamp = 5000L,
            amplitude = 1500.0,
            frequency = 200.0
        )

        // Assert
        assertTrue(snoreEvent.amplitude > 1000.0)
    }

    @Test
    fun `test SnoreEvent equality`() {
        // Arrange
        val event1 = SnoreEvent(
            id = 1L,
            sessionId = 100L,
            timestamp = 5000L,
            amplitude = 500.0,
            frequency = 200.0
        )
        val event2 = SnoreEvent(
            id = 1L,
            sessionId = 100L,
            timestamp = 5000L,
            amplitude = 500.0,
            frequency = 200.0
        )
        val event3 = SnoreEvent(
            id = 2L,
            sessionId = 100L,
            timestamp = 5000L,
            amplitude = 500.0,
            frequency = 200.0
        )

        // Assert
        assertEquals(event1, event2)
        assertNotEquals(event1, event3)
    }

    @Test
    fun `test SnoreEvent timestamp ordering`() {
        // Arrange
        val event1 = SnoreEvent(
            id = 1L,
            sessionId = 100L,
            timestamp = 1000L,
            amplitude = 500.0,
            frequency = 200.0
        )
        val event2 = SnoreEvent(
            id = 2L,
            sessionId = 100L,
            timestamp = 2000L,
            amplitude = 600.0,
            frequency = 250.0
        )

        // Assert
        assertTrue(event2.timestamp > event1.timestamp)
    }

    @Test
    fun `test multiple SnoreEvents for same session`() {
        // Arrange
        val sessionId = 100L
        val events = listOf(
            SnoreEvent(1L, sessionId, 1000L, 500.0, 200.0),
            SnoreEvent(2L, sessionId, 2000L, 600.0, 250.0),
            SnoreEvent(3L, sessionId, 3000L, 550.0, 220.0)
        )

        // Assert
        assertTrue(events.all { it.sessionId == sessionId })
        assertEquals(3, events.size)
    }

    @Test
    fun `test Session with snippet flag`() {
        // Arrange & Act
        val snippetSession = Session(
            id = 1L,
            startTime = 1000L,
            endTime = 2000L,
            snoreScore = 0f,
            fullAudioPath = "/path/to/snippet.pcm",
            isSnippet = true
        )

        // Assert
        assertTrue(snippetSession.isSnippet)
    }

    @Test
    fun `test Session with empty audio path`() {
        // Arrange & Act
        val session = Session(
            id = 1L,
            startTime = 1000L,
            endTime = 2000L,
            snoreScore = 0f,
            fullAudioPath = "",
            isSnippet = false
        )

        // Assert
        assertTrue(session.fullAudioPath.isEmpty())
    }
}
