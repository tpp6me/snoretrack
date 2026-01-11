# SnoreTrack Android App - Technical Overview

## Project Summary

SnoreTrack is a privacy-first Android sleep tracking application that detects and records snoring events using real-time digital signal processing. Built with modern Android technologies including Jetpack Compose, Room Database, and Hilt dependency injection.

**Note:** This app does NOT use a framework called "Antigravity" - it's built with standard Android Jetpack components.

## Architecture

### MVVM + Clean Architecture

```
┌─────────────────────────────────────────────┐
│  UI Layer (Jetpack Compose)                 │
│  - MainScreen.kt                            │
│  - MainViewModel.kt                         │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│  Domain Layer (Business Logic)              │
│  - SnoreAnalyzer.kt (DSP Algorithm)         │
│  - AudioPlayer.kt (PCM Playback)            │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│  Data Layer (Persistence)                   │
│  - AppDatabase.kt (Room)                    │
│  - Entities.kt (Session, SnoreEvent)        │
│  - Daos.kt (Data Access)                    │
└─────────────────────────────────────────────┘
```

### Technology Stack

| Component | Technology |
|-----------|-----------|
| UI Framework | Jetpack Compose + Material Design 3 |
| Language | Kotlin 1.9.22 |
| Async | Kotlin Coroutines 1.7.3 + StateFlow |
| Database | Room 2.6.1 |
| DI | Hilt 2.51 |
| Build System | Gradle (Kotlin DSL) |
| Min SDK | API 26 (Android 8) |
| Target SDK | API 34 (Android 14) |

## Core Features

### 1. Background Audio Recording
- Runs as **Foreground Service** with microphone type notification
- Continuous recording even with screen off
- Sample rate: 44.1 kHz, PCM 16-bit, Mono
- Stores raw uncompressed audio (trade-off: large files for analysis accuracy)

### 2. Real-Time Snore Detection
Uses dual-metric DSP algorithm in `SnoreAnalyzer.kt`:

**RMS (Root Mean Square) - Loudness Detection**
```kotlin
rms = sqrt(sum(samples²) / count)
threshold: > 500.0 (raw PCM values)
```

**Zero-Crossing Rate (ZCR) - Frequency Analysis**
```kotlin
zcr = (zero_crossings × sample_rate) / (2 × buffer_size)
threshold: 0 < zcr < 600 Hz
```

**Snore Classification:**
```
isSnore = (RMS > 500) AND (0 < ZCR < 600 Hz)
```

### 3. Session Tracking
- Automatic session creation on recording start
- Metadata: start time, end time, snore score (avg RMS)
- Full audio + individual snore snippets saved
- 7-day auto-cleanup for snippets

### 4. Audio Playback & Visualization
- Custom PCM audio player (`AudioPlayer.kt`)
- Real-time waveform graph during playback
- Sliding window of 50 latest amplitude points
- Canvas-based gradient visualization

### 5. Privacy-First Design
- All data stored locally in app-specific directory
- No network permissions
- No cloud uploads or analytics
- Files in `getExternalFilesDir()` (cleared on uninstall)

## Data Models

### Database Schema

**Session Entity**
```kotlin
@Entity(tableName = "sessions")
data class Session(
    val id: Long,              // Auto-generated PK
    val startTime: Long,       // Epoch milliseconds
    val endTime: Long,         // Epoch milliseconds
    val snoreScore: Float,     // Average RMS amplitude
    val fullAudioPath: String, // Path to PCM file
    val isSnippet: Boolean     // Reserved for future use
)
```

**SnoreEvent Entity**
```kotlin
@Entity(tableName = "snore_events")
data class SnoreEvent(
    val id: Long,          // Auto-generated PK
    val sessionId: Long,   // FK to Session
    val timestamp: Long,   // Detection time
    val amplitude: Double, // RMS value
    val frequency: Double  // Zero-crossing frequency
)
```

### File Storage

**Format:** Raw PCM (16-bit signed, little-endian)
**Location:** `{externalFilesDir}/snoretrack/`
**Naming:**
- Full sessions: `session_yyyyMMdd_HHmmss.pcm`
- Snippets: `snippet_{sessionId}_{timestamp}.pcm`

**Storage Warning:** 8 hours recording ≈ 2.5GB uncompressed

## Key Components

### Service Layer

**AudioRecorderService.kt** (`service/AudioRecorderService.kt:1`)
- Foreground service with microphone notification
- Audio capture thread on IO dispatcher
- Real-time analysis pipeline
- StateFlow-based status broadcasting
- Handles Android 12+ FOREGROUND_SERVICE_TYPE_MICROPHONE

**Key Methods:**
- `startRecording()` - Initializes AudioRecord, creates session
- `stopRecording()` - Finalizes session, computes score
- `processAudioBuffer()` - Analyzes each chunk with SnoreAnalyzer
- `saveSnippet()` - Writes detected snore to separate file

### Domain Layer

**SnoreAnalyzer.kt** (`domain/SnoreAnalyzer.kt:1`)
- Stateless DSP processor
- `analyze(buffer: ShortArray): AnalysisResult`
- Returns: RMS, ZCR, isSnore boolean
- Zero memory allocation per call (efficient for real-time)

**AudioPlayer.kt** (`domain/AudioPlayer.kt:1`)
- Custom PCM audio player using AudioTrack
- `playPcmFile(path: String)` - Async playback
- `amplitudeFlow: StateFlow<Int>` - Real-time amplitude for UI
- Handles play/pause/stop states

### UI Layer

**MainScreen.kt** (`ui/MainScreen.kt:1`)
- Single-screen Compose UI
- Circular pulsing recording button (scales 1.0-1.1x)
- Session list with expandable playback controls
- Waveform visualization canvas
- Real-time amplitude display during recording

**MainViewModel.kt** (`ui/MainViewModel.kt:1`)
- Bridges Service and UI
- `toggleRecording()` - Starts/stops service via Intent
- `playSession()` / `pausePlayback()` / `stopPlayback()`
- `deleteSession()` - Removes DB record + files
- Manages StateFlow subscriptions for reactive UI

### Data Layer

**AppDatabase.kt** (`data/AppDatabase.kt:1`)
- Room database singleton via Hilt
- Auto-migration support
- Entities: Session, SnoreEvent
- Daos: SessionDao, SnoreEventDao

## Critical Code Flows

### Recording Flow

```
User clicks START
    ↓
Permission Check (RECORD_AUDIO, WAKE_LOCK, POST_NOTIFICATIONS)
    ↓
MainViewModel.toggleRecording()
    ↓
Intent → AudioRecorderService
    ↓
Service.startForeground() with notification
    ↓
AudioRecord initialized (44.1kHz, MONO, 16-bit)
    ↓
Session inserted to DB
    ↓
Audio thread spawned (CoroutineScope + IO dispatcher)
    ↓
Loop: Read buffer → SnoreAnalyzer.analyze()
    ↓
If snore detected → Save snippet + SnoreEvent record
    ↓
Write to full session PCM file
    ↓
Emit AnalysisResult via StateFlow → UI updates
```

### Playback Flow

```
User clicks PLAY on session
    ↓
MainViewModel.playSession(sessionId)
    ↓
AudioPlayer.playPcmFile(path)
    ↓
FileInputStream reads PCM file in chunks
    ↓
AudioTrack.write() plays audio
    ↓
Calculate RMS per chunk → amplitudeFlow
    ↓
MainScreen collects flow → Waveform canvas updates
    ↓
On completion → Reset playback state
```

## UI/UX Details

### Theme
- **Background:** Deep blue gradient (0xFF0F172A → 0xFF1E293B)
- **Accent:** Purple (0xFF8B5CF6)
- **Alert:** Amber (0xFFFBBF24) for snore detection
- **Optimized for nighttime use**

### Animations
- Recording button pulsing (1.0x to 1.1x scale, 1000ms cycle)
- Fade-in/fade-out for status text
- Smooth waveform drawing during playback

### Session Card Layout
- Header: Date, duration, snore score
- Expandable: Play/pause button, delete button
- Waveform graph (when playing)
- Gradient fill + stroke visualization

## Development Setup

### Build Configuration

**build.gradle.kts (app level):**
```kotlin
compileSdk = 34
minSdk = 26
targetSdk = 34
kotlinOptions.jvmTarget = "17"
```

**Key Dependencies:**
```kotlin
// Compose
implementation("androidx.activity:activity-compose:1.9.3")
implementation("androidx.compose.material3:material3")

// Room
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")

// Hilt
implementation("com.google.dagger:hilt-android:2.51")
kapt("com.google.dagger:hilt-android-compiler:2.51")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
```

### Required Permissions

**AndroidManifest.xml:**
```xml
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_MICROPHONE" />
```

## File Structure

```
app/src/main/java/com/praveen/snoretrack/
├── MainActivity.kt                 # Entry point, permission handling
├── SnoreApplication.kt            # Hilt application class
├── data/
│   ├── AppDatabase.kt             # Room database definition
│   ├── Daos.kt                    # SessionDao, SnoreEventDao
│   └── Entities.kt                # Session, SnoreEvent
├── di/
│   └── DatabaseModule.kt          # Hilt DI module for Room
├── domain/
│   ├── SnoreAnalyzer.kt           # DSP algorithm (RMS + ZCR)
│   └── AudioPlayer.kt             # PCM audio playback
├── service/
│   └── AudioRecorderService.kt    # Background recording service
└── ui/
    ├── MainScreen.kt              # Compose UI
    ├── MainViewModel.kt           # MVVM view model
    └── theme/
        ├── Color.kt
        ├── Theme.kt
        └── Type.kt
```

## Performance Considerations

### Memory
- Short-lived audio buffers (no accumulation)
- StateFlow prevents duplicate emissions
- Efficient byte-to-short conversion (`ShortToByte_Twiddle_Method`)
- Coroutine scopes properly cleaned up on destroy

### Storage
- Raw PCM format (large but analysis-friendly)
- Auto-cleanup: Snippets deleted after 7 days
- Latest session full audio preserved
- Consider compression in future versions (MP3/AAC)

### Battery
- Foreground service ensures background survival
- WAKE_LOCK keeps CPU active during recording
- Optimized buffer sizes (calculated dynamically)
- Mono channel reduces processing by 50%

## Known Limitations

1. **Storage intensive** - 8 hours ≈ 2.5GB (PCM format)
2. **Single-screen UI** - No detailed analytics or graphs
3. **No compression** - Files stored as raw PCM
4. **Basic snore detection** - Simple threshold-based (no ML)
5. **No export feature** - Data locked to device
6. **Mono audio only** - Cannot detect which side user is sleeping on

## Future Enhancement Ideas

- [ ] Compress audio files (MP3/AAC encoding)
- [ ] Machine learning snore classifier
- [ ] Export sessions to CSV/JSON
- [ ] Sleep quality score algorithm
- [ ] Multi-night trend analysis
- [ ] Integration with sleep position sensors
- [ ] Cloud backup option (with encryption)
- [ ] Wear OS companion app

## Testing

**Unit Tests:**
- `ExampleUnitTest.kt` - JUnit 4 placeholder
- Add SnoreAnalyzer algorithm tests (RMS, ZCR calculations)
- Add Session/SnoreEvent entity tests

**UI Tests:**
- `ExampleInstrumentedTest.kt` - Espresso placeholder
- Add MainScreen compose tests
- Add recording flow integration tests

## Code Quality Notes

**Strengths:**
- Clean architecture separation
- Proper dependency injection
- Kotlin coroutines for async operations
- StateFlow for reactive UI
- Type-safe Compose UI
- Comprehensive README documentation

**Areas for Improvement:**
- Add unit tests for SnoreAnalyzer
- Extract magic numbers to constants (500.0, 600.0 thresholds)
- Add error handling for file I/O operations
- Consider repository pattern for data layer abstraction
- Add logging framework for debugging
- Document public API with KDoc comments

## Debugging Tips

**View real-time logs:**
```bash
adb logcat | grep "SnoreTrack\|AudioRecorder\|SnoreAnalyzer"
```

**Check database:**
```bash
adb shell
run-as com.praveen.snoretrack
cd databases
sqlite3 snoretrack_database
.tables
SELECT * FROM sessions;
```

**Check audio files:**
```bash
adb shell ls -lh /storage/emulated/0/Android/data/com.praveen.snoretrack/files/
```

**Pull session file for analysis:**
```bash
adb pull /storage/emulated/0/Android/data/com.praveen.snoretrack/files/session_*.pcm
# Analyze with Audacity (import raw: 44100Hz, 16-bit signed, little-endian, mono)
```

## References

- **Project README:** `/Users/praveen/work/github/snoretrack/README.md`
- **Main Service:** `app/src/main/java/com/praveen/snoretrack/service/AudioRecorderService.kt:1`
- **DSP Algorithm:** `app/src/main/java/com/praveen/snoretrack/domain/SnoreAnalyzer.kt:7`
- **UI Layer:** `app/src/main/java/com/praveen/snoretrack/ui/MainScreen.kt:1`
- **Database:** `app/src/main/java/com/praveen/snoretrack/data/AppDatabase.kt:1`

---

**Last Updated:** January 11, 2026
**Author:** Praveen
**License:** Not specified in codebase
