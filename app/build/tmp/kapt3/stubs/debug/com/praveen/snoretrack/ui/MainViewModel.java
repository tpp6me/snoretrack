package com.praveen.snoretrack.ui;

import android.content.Context;
import android.content.Intent;
import androidx.lifecycle.ViewModel;
import com.praveen.snoretrack.data.Session;
import com.praveen.snoretrack.data.SessionDao;
import com.praveen.snoretrack.domain.AnalysisResult;
import com.praveen.snoretrack.domain.AudioPlayer;
import com.praveen.snoretrack.service.AudioRecorderService;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import kotlinx.coroutines.flow.SharedFlow;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.Dispatchers;
import java.io.File;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B!\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0001\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u000e\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001aJ\u000e\u0010\u001f\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001aJ\u0006\u0010 \u001a\u00020\u001dJ\u0006\u0010!\u001a\u00020\u001dR\u0016\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000e0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0011\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0010R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0010R\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u00190\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0010\u00a8\u0006\""}, d2 = {"Lcom/praveen/snoretrack/ui/MainViewModel;", "Landroidx/lifecycle/ViewModel;", "sessionDao", "Lcom/praveen/snoretrack/data/SessionDao;", "audioPlayer", "Lcom/praveen/snoretrack/domain/AudioPlayer;", "context", "Landroid/content/Context;", "(Lcom/praveen/snoretrack/data/SessionDao;Lcom/praveen/snoretrack/domain/AudioPlayer;Landroid/content/Context;)V", "_currentlyPlayingSessionId", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "analysisResult", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/praveen/snoretrack/domain/AnalysisResult;", "getAnalysisResult", "()Lkotlinx/coroutines/flow/StateFlow;", "currentlyPlayingSessionId", "getCurrentlyPlayingSessionId", "isRecording", "", "playbackAmplitude", "", "getPlaybackAmplitude", "sessions", "", "Lcom/praveen/snoretrack/data/Session;", "getSessions", "deleteSession", "", "session", "playSession", "stopPlayback", "toggleRecording", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class MainViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.praveen.snoretrack.data.SessionDao sessionDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.praveen.snoretrack.domain.AudioPlayer audioPlayer = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isRecording = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.praveen.snoretrack.domain.AnalysisResult> analysisResult = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.praveen.snoretrack.data.Session>> sessions = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Long> _currentlyPlayingSessionId = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Long> currentlyPlayingSessionId = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Float> playbackAmplitude = null;
    
    @javax.inject.Inject()
    public MainViewModel(@org.jetbrains.annotations.NotNull()
    com.praveen.snoretrack.data.SessionDao sessionDao, @org.jetbrains.annotations.NotNull()
    com.praveen.snoretrack.domain.AudioPlayer audioPlayer, @dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isRecording() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.praveen.snoretrack.domain.AnalysisResult> getAnalysisResult() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.praveen.snoretrack.data.Session>> getSessions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Long> getCurrentlyPlayingSessionId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Float> getPlaybackAmplitude() {
        return null;
    }
    
    public final void toggleRecording() {
    }
    
    public final void playSession(@org.jetbrains.annotations.NotNull()
    com.praveen.snoretrack.data.Session session) {
    }
    
    public final void stopPlayback() {
    }
    
    public final void deleteSession(@org.jetbrains.annotations.NotNull()
    com.praveen.snoretrack.data.Session session) {
    }
}