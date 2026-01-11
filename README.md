# SnoreTrack 💤

SnoreTrack is an Android application designed to track, analyze, and visualize sleep quality by detecting snore patterns. It records audio in the background, processes it locally to ensure privacy, and provides detailed insights into sleep habits.

## 🚀 Key Features

*   **Background Recording**: Reliable audio recording service that runs as a Foreground Service, ensuring it continues even when the screen is off or the app is minimized.
*   **Privacy First**: All audio processing and storage happen locally on the device. No data is uploaded to the cloud.
*   **Smart Analysis**: Uses Digital Signal Processing (DSP) to detect snores in real-time based on amplitude and frequency thresholds.
*   **Visual Feedback**:
    *   **Live Waveform**: Visualize the audio levels in real-time during playback with a beautiful sliding window graph.
    *   **Pulsing UI**: Immediate visual feedback when recording is active.
*   **Session History**: Keeps track of all your sleep sessions, including duration, date, and a computed "Snore Score".
*   **Playback & Management**: Listen to past recordings and easily manage storage by deleting old sessions.
*   **Night Mode**: A sleek, dark-themed UI designed to be easy on the eyes in low-light environments.

## 🛠️ Architecture & Tech Stack

The app is built using modern Android development practices:

*   **Language**: Kotlin
*   **UI Toolkit**: Jetpack Compose (Material3)
*   **Dependency Injection**: Hilt
*   **Asynchronous Processing**: Kotlin Coroutines & Flows
*   **Local Database**: Room Persistence Library
*   **Architecture Pattern**: MVVM (Model-View-ViewModel) with Clean Architecture principles.

### Key Components

*   **`AudioRecorderService`**: The core service managing audio capture, analyzing signals via `SnoreAnalyzer`, and handling file I/O.
*   **`SnoreAnalyzer`**: Domain logic for classifying specific audio signals as snores using RMS and Zero-Crossing Rate.
*   **`MainViewModel`**: Manages UI state and bridges the data layer with the Compose UI.
*   **`AudioPlayer`**: Custom implementation using `AudioTrack` to play raw PCM audio data and emit real-time amplitude for visualization.

## 📱 Getting Started

### Prerequisites
*   Android Studio Iguana or later
*   JDK 17
*   Android Device or Emulator running Android 10 (API 29) or higher (Target SDK is Android 14).

### Building the Project
1.  **Clone the repository**:
    ```bash
    git clone https://github.com/your-username/snoretrack.git
    ```
2.  **Open in Android Studio**.
3.  **Sync Gradle** to download dependencies.
4.  **Run** the app on your connected device or emulator.

**Note**: You will need to grant **Microphone** and **Notification** permissions upon first launch for the app to function correctly.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
