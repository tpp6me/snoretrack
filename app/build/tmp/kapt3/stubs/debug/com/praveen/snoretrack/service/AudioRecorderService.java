package com.praveen.snoretrack.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import androidx.core.app.ServiceCompat;
import com.praveen.snoretrack.R;
import com.praveen.snoretrack.data.Session;
import com.praveen.snoretrack.data.SessionDao;
import com.praveen.snoretrack.domain.AnalysisResult;
import com.praveen.snoretrack.domain.SnoreAnalyzer;
import dagger.hilt.android.AndroidEntryPoint;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.StateFlow;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.inject.Inject;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0084\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u0017\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u0000 :2\u00020\u0001:\u0001:B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u0004H\u0002J\u0016\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020\nH\u0082@\u00a2\u0006\u0002\u0010&J\b\u0010\'\u001a\u00020(H\u0002J\b\u0010)\u001a\u00020$H\u0002J\b\u0010*\u001a\u00020\bH\u0002J\u0014\u0010+\u001a\u0004\u0018\u00010,2\b\u0010-\u001a\u0004\u0018\u00010.H\u0016J\b\u0010/\u001a\u00020$H\u0016J\"\u00100\u001a\u00020\u00042\b\u0010-\u001a\u0004\u0018\u00010.2\u0006\u00101\u001a\u00020\u00042\u0006\u00102\u001a\u00020\u0004H\u0016J\u0018\u00103\u001a\u00020$2\u0006\u00104\u001a\u00020!2\u0006\u00105\u001a\u000206H\u0002J\b\u00107\u001a\u00020$H\u0002J\b\u00108\u001a\u00020$H\u0002J\b\u00109\u001a\u00020$H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0013\u001a\u00020\u00148\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u000e\u0010\u0019\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006;"}, d2 = {"Lcom/praveen/snoretrack/service/AudioRecorderService;", "Landroid/app/Service;", "()V", "analysisCount", "", "audioRecord", "Landroid/media/AudioRecord;", "currentSessionFile", "Ljava/io/File;", "currentSessionId", "", "fileOutputStream", "Ljava/io/FileOutputStream;", "isRecording", "", "serviceJob", "Lkotlinx/coroutines/CompletableJob;", "serviceScope", "Lkotlinx/coroutines/CoroutineScope;", "sessionDao", "Lcom/praveen/snoretrack/data/SessionDao;", "getSessionDao", "()Lcom/praveen/snoretrack/data/SessionDao;", "setSessionDao", "(Lcom/praveen/snoretrack/data/SessionDao;)V", "sessionStartTime", "snoreAnalyzer", "Lcom/praveen/snoretrack/domain/SnoreAnalyzer;", "totalAmplitude", "", "ShortToByte_Twiddle_Method", "", "input", "", "elements", "cleanupOldAudio", "", "latestSessionId", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createNotification", "Landroid/app/Notification;", "createNotificationChannel", "createSessionFile", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onDestroy", "onStartCommand", "flags", "startId", "saveSnippet", "buffer", "result", "Lcom/praveen/snoretrack/domain/AnalysisResult;", "startAudioLoop", "startRecording", "stopRecording", "Companion", "app_debug"})
public final class AudioRecorderService extends android.app.Service {
    @javax.inject.Inject()
    public com.praveen.snoretrack.data.SessionDao sessionDao;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CompletableJob serviceJob = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope serviceScope = null;
    @org.jetbrains.annotations.Nullable()
    private android.media.AudioRecord audioRecord;
    private boolean isRecording = false;
    @org.jetbrains.annotations.NotNull()
    private final com.praveen.snoretrack.domain.SnoreAnalyzer snoreAnalyzer = null;
    @org.jetbrains.annotations.Nullable()
    private java.io.FileOutputStream fileOutputStream;
    @org.jetbrains.annotations.Nullable()
    private java.io.File currentSessionFile;
    private long sessionStartTime = 0L;
    private long currentSessionId = 0L;
    private double totalAmplitude = 0.0;
    private int analysisCount = 0;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableStateFlow<com.praveen.snoretrack.domain.AnalysisResult> _analysisState = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.StateFlow<com.praveen.snoretrack.domain.AnalysisResult> analysisState = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isRecordingState = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isRecordingState = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_START = "ACTION_START";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_STOP = "ACTION_STOP";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String CHANNEL_ID = "SnoreTrackChannel";
    private static final int NOTIFICATION_ID = 1;
    private static final int SAMPLE_RATE = 44100;
    @org.jetbrains.annotations.NotNull()
    public static final com.praveen.snoretrack.service.AudioRecorderService.Companion Companion = null;
    
    public AudioRecorderService() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.praveen.snoretrack.data.SessionDao getSessionDao() {
        return null;
    }
    
    public final void setSessionDao(@org.jetbrains.annotations.NotNull()
    com.praveen.snoretrack.data.SessionDao p0) {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public android.os.IBinder onBind(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent) {
        return null;
    }
    
    @java.lang.Override()
    public int onStartCommand(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    private final void startRecording() {
    }
    
    private final void startAudioLoop() {
    }
    
    private final void saveSnippet(short[] buffer, com.praveen.snoretrack.domain.AnalysisResult result) {
    }
    
    private final byte[] ShortToByte_Twiddle_Method(short[] input, int elements) {
        return null;
    }
    
    private final java.io.File createSessionFile() {
        return null;
    }
    
    private final void stopRecording() {
    }
    
    private final java.lang.Object cleanupOldAudio(long latestSessionId, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final void createNotificationChannel() {
    }
    
    private final android.app.Notification createNotification() {
        return null;
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u0016\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u000f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0012\u00a8\u0006\u0014"}, d2 = {"Lcom/praveen/snoretrack/service/AudioRecorderService$Companion;", "", "()V", "ACTION_START", "", "ACTION_STOP", "CHANNEL_ID", "NOTIFICATION_ID", "", "SAMPLE_RATE", "_analysisState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/praveen/snoretrack/domain/AnalysisResult;", "_isRecordingState", "", "analysisState", "Lkotlinx/coroutines/flow/StateFlow;", "getAnalysisState", "()Lkotlinx/coroutines/flow/StateFlow;", "isRecordingState", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final kotlinx.coroutines.flow.StateFlow<com.praveen.snoretrack.domain.AnalysisResult> getAnalysisState() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isRecordingState() {
            return null;
        }
    }
}