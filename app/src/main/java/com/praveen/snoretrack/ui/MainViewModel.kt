package com.praveen.snoretrack.ui

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.praveen.snoretrack.data.Session
import com.praveen.snoretrack.data.SessionDao
import com.praveen.snoretrack.domain.AnalysisResult
import com.praveen.snoretrack.domain.AudioPlayer
import com.praveen.snoretrack.service.AudioRecorderService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionDao: SessionDao,
    private val audioPlayer: AudioPlayer,
    @ApplicationContext private val context: Context
) : ViewModel() {

    // observe service state
    val isRecording: StateFlow<Boolean> = AudioRecorderService.isRecordingState
    val analysisResult: StateFlow<AnalysisResult?> = AudioRecorderService.analysisState
    
    // History
    val sessions: StateFlow<List<Session>> = sessionDao.getAllSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Playback State
    private val _currentlyPlayingSessionId = MutableStateFlow<Long?>(null)
    val currentlyPlayingSessionId: StateFlow<Long?> = _currentlyPlayingSessionId.asStateFlow()
    
    val playbackAmplitude: StateFlow<Float> = audioPlayer.amplitudeFlow

    fun toggleRecording() {
        val currentState = isRecording.value
        val intent = Intent(context, AudioRecorderService::class.java)
        if (currentState) {
            intent.action = AudioRecorderService.ACTION_STOP
        } else {
            intent.action = AudioRecorderService.ACTION_START
        }
        context.startService(intent)
    }
    
    fun playSession(session: Session) {
        if (_currentlyPlayingSessionId.value == session.id) {
            // Stop if already playing this one
            stopPlayback()
        } else {
            _currentlyPlayingSessionId.value = session.id
            viewModelScope.launch {
                audioPlayer.playPcmFile(session.fullAudioPath)
                _currentlyPlayingSessionId.value = null // Reset when done
            }
        }
    }
    
    fun stopPlayback() {
         audioPlayer.stop()
         _currentlyPlayingSessionId.value = null
    }
    
    fun deleteSession(session: Session) {
        viewModelScope.launch(Dispatchers.IO) {
            // Stop if playing this one
            if (_currentlyPlayingSessionId.value == session.id) {
                stopPlayback()
            }
            
            // Delete file
            if (session.fullAudioPath.isNotEmpty()) {
                val file = File(session.fullAudioPath)
                if (file.exists()) {
                    file.delete()
                }
            }
            
            // Delete from DB
            sessionDao.deleteSession(session)
        }
    }
}
