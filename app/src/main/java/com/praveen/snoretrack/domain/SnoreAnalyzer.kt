package com.praveen.snoretrack.domain

import kotlin.math.sqrt

data class AnalysisResult(
    val isSnore: Boolean,
    val amplitude: Double, // RMS
    val frequency: Double // Approximate Hz via Zero Crossing
)

class SnoreAnalyzer {

    // Thresholds - these would ideally be calibrated or user-adjustable
    // Snores are typically high amplitude and low frequency (e.g., < 500Hz)
    private val AMPLITUDE_THRESHOLD = 500.0 // Raw PCM 16-bit typical quiet room is < 100
    private val FREQUENCY_THRESHOLD = 600.0 // Hz

    fun analyze(buffer: ShortArray, sampleRate: Int): AnalysisResult {
        val rms = calculateRMS(buffer)
        val zcrFreq = calculateZeroCrossingRate(buffer, sampleRate)

        val isSnore = rms > AMPLITUDE_THRESHOLD && zcrFreq > 0 && zcrFreq < FREQUENCY_THRESHOLD

        return AnalysisResult(isSnore, rms, zcrFreq)
    }

    private fun calculateRMS(buffer: ShortArray): Double {
        var sum = 0.0
        for (sample in buffer) {
            sum += sample * sample
        }
        return sqrt(sum / buffer.size)
    }

    private fun calculateZeroCrossingRate(buffer: ShortArray, sampleRate: Int): Double {
        var zeroCrossings = 0
        for (i in 0 until buffer.size - 1) {
            if ((buffer[i] >= 0 && buffer[i + 1] < 0) || (buffer[i] < 0 && buffer[i + 1] >= 0)) {
                zeroCrossings++
            }
        }
        return (zeroCrossings * sampleRate) / (2.0 * buffer.size)
    }
}
