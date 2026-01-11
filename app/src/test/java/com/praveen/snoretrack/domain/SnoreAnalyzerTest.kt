package com.praveen.snoretrack.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.math.sqrt

class SnoreAnalyzerTest {

    private lateinit var snoreAnalyzer: SnoreAnalyzer

    @Before
    fun setup() {
        snoreAnalyzer = SnoreAnalyzer()
    }

    @Test
    fun `test silent audio returns low RMS`() {
        // Arrange: Silent audio buffer (all zeros)
        val silentBuffer = ShortArray(1000) { 0 }

        // Act
        val result = snoreAnalyzer.analyze(silentBuffer, sampleRate = 44100)

        // Assert
        assertEquals(0.0, result.amplitude, 0.01)
        assertFalse(result.isSnore)
    }

    @Test
    fun `test loud audio returns high RMS`() {
        // Arrange: Loud audio buffer (max amplitude)
        val loudBuffer = ShortArray(1000) { 1000 }

        // Act
        val result = snoreAnalyzer.analyze(loudBuffer, sampleRate = 44100)

        // Assert
        assertTrue(result.amplitude > 500.0)
    }

    @Test
    fun `test snore detection with appropriate RMS and frequency`() {
        // Arrange: Simulate snore-like pattern
        // Low frequency (50 Hz), high amplitude
        val bufferSize = 882 // 0.02 seconds at 44100 Hz
        val frequency = 50.0 // Low frequency typical of snores
        // RMS of sine wave = amplitude / sqrt(2), so amplitude = RMS * sqrt(2)
        // We need RMS > 500, so amplitude needs to be > 707
        val amplitude = 900.0 // Above threshold

        val snoreBuffer = ShortArray(bufferSize) { i ->
            (amplitude * Math.sin(2.0 * Math.PI * frequency * i / 44100)).toInt().toShort()
        }

        // Act
        val result = snoreAnalyzer.analyze(snoreBuffer, sampleRate = 44100)

        // Assert
        assertTrue("RMS should be above threshold", result.amplitude > 500.0)
        assertTrue("Frequency should be in snore range (0-600 Hz)", result.frequency in 0.0..600.0)
        assertTrue("Should detect as snore", result.isSnore)
    }

    @Test
    fun `test high frequency audio is not detected as snore`() {
        // Arrange: High frequency audio (above 600 Hz)
        val bufferSize = 882
        val frequency = 1000.0 // High frequency (not a snore)
        val amplitude = 900.0 // High amplitude (RMS will be ~636)

        val highFreqBuffer = ShortArray(bufferSize) { i ->
            (amplitude * Math.sin(2.0 * Math.PI * frequency * i / 44100)).toInt().toShort()
        }

        // Act
        val result = snoreAnalyzer.analyze(highFreqBuffer, sampleRate = 44100)

        // Assert
        assertTrue("RMS should be above threshold", result.amplitude > 500.0)
        assertTrue("Frequency should be above snore range", result.frequency > 600.0)
        assertFalse("Should NOT detect as snore due to high frequency", result.isSnore)
    }

    @Test
    fun `test low amplitude audio is not detected as snore`() {
        // Arrange: Low amplitude audio
        val bufferSize = 882
        val frequency = 100.0 // Low frequency (good)
        val amplitude = 100.0 // Low amplitude (below threshold)

        val lowAmpBuffer = ShortArray(bufferSize) { i ->
            (amplitude * Math.sin(2.0 * Math.PI * frequency * i / 44100)).toInt().toShort()
        }

        // Act
        val result = snoreAnalyzer.analyze(lowAmpBuffer, sampleRate = 44100)

        // Assert
        assertTrue("RMS should be below threshold", result.amplitude < 500.0)
        assertFalse("Should NOT detect as snore due to low amplitude", result.isSnore)
    }

    @Test
    fun `test zero crossing rate calculation`() {
        // Arrange: Alternating signal (max zero crossings)
        val alternatingBuffer = ShortArray(1000) { i ->
            if (i % 2 == 0) 100 else -100
        }

        // Act
        val result = snoreAnalyzer.analyze(alternatingBuffer, sampleRate = 44100)

        // Assert: High zero crossing rate
        assertTrue("Frequency should be very high due to alternating signal", result.frequency > 10000.0)
    }

    @Test
    fun `test RMS calculation accuracy`() {
        // Arrange: Buffer with known values
        val testBuffer = shortArrayOf(100, 200, 300, 400, 500)

        // Calculate expected RMS manually
        val sumOfSquares = testBuffer.map { it.toDouble() * it.toDouble() }.sum()
        val expectedRms = sqrt(sumOfSquares / testBuffer.size)

        // Act
        val result = snoreAnalyzer.analyze(testBuffer, sampleRate = 44100)

        // Assert
        assertEquals(expectedRms, result.amplitude, 0.1)
    }

    @Test
    fun `test empty buffer handling`() {
        // Arrange
        val emptyBuffer = ShortArray(0)

        // Act & Assert: Empty buffer causes divide by zero, returns NaN
        try {
            val result = snoreAnalyzer.analyze(emptyBuffer, sampleRate = 44100)
            // Empty buffer will return NaN for RMS (sqrt(0/0))
            assertTrue("Result should contain NaN for amplitude", result.amplitude.isNaN())
            assertFalse(result.isSnore)
        } catch (e: Exception) {
            // If it throws, that's also acceptable behavior
            assertTrue("Should handle empty buffer gracefully", true)
        }
    }

    @Test
    fun `test single sample buffer`() {
        // Arrange
        val singleBuffer = shortArrayOf(500)

        // Act
        val result = snoreAnalyzer.analyze(singleBuffer, sampleRate = 44100)

        // Assert
        assertEquals(500.0, result.amplitude, 0.1)
        assertEquals(0.0, result.frequency, 0.1) // No zero crossings possible
        assertFalse(result.isSnore) // Zero frequency, so not a snore
    }

    @Test
    fun `test boundary RMS threshold`() {
        // Arrange: Buffer with RMS exactly at threshold
        val bufferSize = 100
        // RMS = 500 means sqrt(sum(x^2)/n) = 500
        // So sum(x^2) = 500^2 * n = 250000 * 100
        val targetRms = 500.0
        val amplitude = (targetRms * sqrt(2.0)).toInt() // For sine wave

        val boundaryBuffer = ShortArray(bufferSize) { i ->
            (amplitude * Math.sin(2.0 * Math.PI * 100.0 * i / 44100)).toInt().toShort()
        }

        // Act
        val result = snoreAnalyzer.analyze(boundaryBuffer, sampleRate = 44100)

        // Assert: RMS should be around threshold
        assertTrue("RMS should be around threshold", result.amplitude >= 400.0 && result.amplitude <= 600.0)
    }

    @Test
    fun `test negative samples do not affect RMS calculation`() {
        // Arrange: Mix of positive and negative samples
        val mixedBuffer = ShortArray(1000) { i ->
            if (i % 2 == 0) 600 else -600
        }

        // Act
        val result = snoreAnalyzer.analyze(mixedBuffer, sampleRate = 44100)

        // Assert: RMS should be same as all positive 600s
        assertEquals(600.0, result.amplitude, 1.0)
    }
}
