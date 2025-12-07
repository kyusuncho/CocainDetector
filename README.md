# CocaineDetector

An Android app that uses `AccessibilityService` to monitor the screen for the keyword "cocaine". When detected, it logs the event and takes a screenshot.

## Features
- **Accessibility Service**: Monitors text across all apps.
- **Keyword Detection**: Triggers on "cocaine" (case-insensitive).
- **Screenshot Capture**: Uses `MediaProjection` API to capture the screen.
- **Logging**: Saves detection timestamp, package name, and screenshot to a local Room database.
- **UI**: View list of detections with screenshots.

## Setup & Usage

1.  **Open in Android Studio**: Open this project directory.
2.  **Build**: Sync Gradle and build the app.
3.  **Run**: Deploy to an Android device or emulator (API 24+ recommended).
4.  **Permissions**:
    - Open the app.
    - Click **Enable Accessibility** -> Find "Cocaine Detector Service" -> Enable it.
    - Click **Enable Screenshot** -> Grant the "Start recording or casting" permission.
5.  **Test**:
    - Switch to another app (e.g., Chrome, Messages).
    - Type or view the word "cocaine".
    - You should see a notification (if enabled) or check the main app for the log.

## Notes
- **Privacy**: This app monitors screen content. Use responsibly.
- **Android 14**: Requires `FOREGROUND_SERVICE_MEDIA_PROJECTION` permission (included in Manifest).
- **Screenshots**: The screenshot feature requires the user to explicitly grant permission every time the app process restarts (unless the service stays alive).
