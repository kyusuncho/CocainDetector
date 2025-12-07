# CocaineDetector

An Android app that uses `AccessibilityService` to monitor on-screen text across all apps for the keyword **"cocaine"** (case-insensitive). When detected, it logs the event (including the full captured text) to a local Room database, which you can browse inside the app.

## Current Features

- **Accessibility Service**
  - Runs as an `AccessibilityService` and can read text from other apps' UI (where exposed to accessibility).
  - Checks the current screen at most **once every 2 seconds** to avoid excessive work.

- **Keyword Detection**
  - Collects all visible text from the active window by recursively traversing the `AccessibilityNodeInfo` tree.
  - Triggers when the combined text contains the word **"cocaine"** (case-insensitive).

- **Logging (Room Database)**
  - Each detection is stored as a `Detection` entity with:
    - `timestamp`: time of detection (epoch millis)
    - `packageName`: app where the word was seen (e.g. `com.google.android.youtube`)
    - `screenText`: full captured text from the screen at that moment
    - `detectedText`: the matched keyword (currently hard-coded to `"cocaine"`)

- **UI (Jetpack Compose)**
  - Main screen shows a **list of detections**, ordered newest-first.
  - Each item displays:
    - App package name
    - Formatted detection timestamp
  - **Tap any detection** to open a dialog showing the **full `screenText`** that was captured at that time.

> Note: Screenshot / MediaProjection features have been **removed**. The app now persists text only.

## Setup & Usage

1. **Open in Android Studio**  
   Open this project directory (`planty-intercepter`) as an Android Studio project.

2. **Build**  
   Sync Gradle and build the app. The project currently uses:
   - `compileSdk = 34`
   - `targetSdk = 33`
   - Android Gradle Plugin 8.1.x

3. **Run**  
   Deploy to a physical device or emulator running **API 24+**.

4. **Enable the Accessibility Service**  
   - Launch the **CocaineDetector** app.
   - Tap **"Enable Accessibility"**.
   - In system Accessibility settings, find **"Cocaine Detector Service"** and enable it.

5. **Test**  
   - Switch to another app (e.g., Chrome, Messages, YouTube) and display or type the word **"cocaine"**.
   - Wait briefly (checks happen every ~2 seconds).
   - Return to the **CocaineDetector** app.
   - You should see one or more entries in the detections list; tap an item to view the full captured text.

## Implementation Notes

- The accessibility configuration is defined in `res/xml/accessibility_config.xml` and uses:
  - `android:accessibilityEventTypes="typeAllMask"` to receive a broad set of events.
  - `android:accessibilityFlags="flagIncludeNotImportantViews|flagReportViewIds"` to include more views and view IDs.
  - `android:canRetrieveWindowContent="true"` so the service can traverse the node tree.

- The service (`CocaineDetectorService`) uses `rootInActiveWindow`, calls `refresh()` on it, and then recursively traverses the node tree to build a `screenText` string.

- Detections are stored via Room (`Detection`, `DetectionDao`, `AppDatabase`) and exposed as a `Flow<List<Detection>>` that the Compose UI collects with `collectAsStateWithLifecycle`.

## Privacy & Responsibility

- This app reads on-screen text from **other apps** using the accessibility APIs.  
  Use it only on your own devices and in compliance with local laws and platform policies.
- The captured text is stored **locally on the device** in a Room database used only by this app.
