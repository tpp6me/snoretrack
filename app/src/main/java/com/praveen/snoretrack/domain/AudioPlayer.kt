package com.praveen.snoretrack.domain

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject

class AudioPlayer @Inject constructor() {

    private var audioTrack: AudioTrack? = null
    private var isPlaying = false

    private val _amplitudeFlow = kotlinx.coroutines.flow.MutableStateFlow(0f)
    val amplitudeFlow: kotlinx.coroutines.flow.StateFlow<Float> = _amplitudeFlow

    suspend fun playPcmFile(filePath: String) = withContext(Dispatchers.IO) {
        if (isPlaying) {
            stop()
        }

        val file = File(filePath)
        if (!file.exists()) return@withContext

        val minBufferSize = AudioTrack.getMinBufferSize(
            44100,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(44100)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(minBufferSize)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()

        audioTrack?.play()
        isPlaying = true

        try {
            FileInputStream(file).use { fis ->
                val buffer = ByteArray(minBufferSize)
                var bytesRead: Int = 0
                while (isPlaying && fis.read(buffer).also { bytesRead = it } != -1) {
                    audioTrack?.write(buffer, 0, bytesRead)
                    
                    // Calculate Amplitude (RMS) for visualization
                    // Convert byte array to short array for calculation
                    var sum = 0.0
                    for (i in 0 until bytesRead step 2) {
                        val sample = (buffer[i].toInt() and 0xFF) or (buffer[i+1].toInt() shl 8)
                        val shortSample = sample.toShort()
                        sum += shortSample * shortSample
                    }
                    val rms = kotlin.math.sqrt(sum / (bytesRead / 2)).toFloat()
                    _amplitudeFlow.value = rms
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            stop()
        }
    }

    fun stop() {
        isPlaying = false
        _amplitudeFlow.value = 0f
        try {
            audioTrack?.stop()
            audioTrack?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        audioTrack = null
    }
}
