# PLAN.md - Tiny Grok Android Client

## Project Overview
Tiny Grok is a lightweight Android client for interacting with xAI's Grok AI models. It provides a simple chat interface to converse with Grok, with support for API key configuration and multiple themes.

## Features
- **Chat Interface**: Real-time conversation with Grok using streaming or standard completions.
- **API Integration**: Uses xAI API (https://api.x.ai) with Grok 4.3 model (model ID: `grok-3` or latest equivalent; configurable).
- **Settings Screen**:
  - Enter and save xAI API key securely (using EncryptedSharedPreferences or DataStore).
  - Select theme: Light, Tokyo Night (custom dark with neon accents), Dark.
  - Model selection if needed (default to Grok 4.3).
- **Themes**:
  - Light: Standard Material Light theme.
  - Dark: Standard Material Dark.
  - Tokyo Night: Custom theme inspired by Tokyo Night VSCode theme - dark background (#1a1b26), purple accents (#bb9af7), cyan highlights (#7aa2f7).
- **UI/UX**:
  - Bottom navigation or drawer for Chat and Settings.
  - Message bubbles with user/assistant distinction.
  - Loading indicators, error handling.
  - Copy message, regenerate response.
- **Build & Distribution**:
  - APK named `tiny-grok-v0.0.1-release(arm64-v8a,armeabi-v7a,x86,x86_64).apk` placed in project root.
  - Support for `--emulate` flag in build.sh to install and run in Android Emulator.
  - `--info` flag for verbose Gradle build logs.

## Tech Stack
- Language: Kotlin
- UI: Jetpack Compose (modern, recommended for new Android apps)
- Networking: Retrofit + OkHttp + Kotlinx Serialization for xAI API calls.
- Persistence: DataStore for settings (API key, theme preference).
- Architecture: MVVM with ViewModels, Repository pattern.
- Gradle: Latest compatible AGP (Android Gradle Plugin 8.x), Kotlin 1.9+.
- Min SDK: 24, Target SDK: 34.

## Implementation Steps
1. **Setup Project Structure**:
   - Ensure standard Android app module under `app/`.
   - Update `build.gradle.kts` files for Compose, Retrofit, etc.
   - Fix any Gradle compatibility issues (e.g., update plugins, remove deprecated `buildDir` usage).

2. **Create Core Files**:
   - `MainActivity.kt`: Entry point with navigation.
   - `ChatScreen.kt`: Compose UI for chat.
   - `SettingsScreen.kt`: UI for API key input and theme picker.
   - `GrokApiService.kt`: Retrofit interface for xAI chat completions.
   - `Theme.kt`: Define light, dark, TokyoNight color schemes and MaterialTheme.
   - `DataStoreManager.kt` or `SettingsRepository.kt`: Handle persistent settings.

3. **API Details**:
   - Endpoint: `POST https://api.x.ai/v1/chat/completions`
   - Headers: `Authorization: Bearer <API_KEY>`, `Content-Type: application/json`
   - Model: `grok-3` (or `grok-beta` for latest; use "grok-3" as per spec for Grok 4.3 equivalent).
   - Request body: messages array, model, temperature, etc.
   - Handle responses, errors (e.g., invalid key).

4. **Theming**:
   - Use `CompositionLocalProvider` or `MaterialTheme` with custom `ColorScheme`.
   - Tokyo Night colors:
     - Background: #1a1b26
     - Surface: #16161e
     - Primary: #bb9af7
     - Secondary: #7aa2f7
     - etc.
   - Persist theme choice and apply on app start.

5. **Build Script (build.sh)**:
   - Default: Clean, build release APK with universal/ABI splits.
   - `--info`: Run Gradle with `--info` flag for debug output.
   - `--emulate`: After build, use `adb` to install APK to running emulator and launch the app.
   - Output APK to root: `tiny-grok-v0.0.1-release(arm64-v8a,armeabi-v7a,x86,x86_64).apk`
   - Version: Hardcode v0.0.1 for now.

6. **.gitignore Updates**:
   - Add standard Android ignores if missing: `*.apk`, `*.aab`, `build/`, `.gradle/`, `local.properties`, `*.iml`, etc.
   - Also ignore any temp files, API key examples.

7. **README.md**:
   - Project description, setup instructions, how to build/run, screenshots placeholders, API key note (user must provide own xAI key).

8. **Folder Rename**:
   - Rename the `aicoder` parent folder (e.g., from `/Users/aicoder/src/android-tiny-grok` to `/Users/dev/src/tiny-grok-android` or similar neutral name). Use shell `mv` command. Update any paths if needed.

9. **Testing & Polish**:
   - Handle no API key case (prompt in settings).
   - Basic error toasts.
   - Ensure APK is universal for multiple ABIs.

## Notes
- Grok 4.3 refers to using the latest Grok model via xAI API (as of spec: target `grok-3`).
- Security: Never hardcode API key; always user-provided.
- This plan will be read and fully implemented by creating/editing all necessary source files, fixing builds, and updating scripts.
- After implementation, verify with `./build.sh --info` and `./build.sh --emulate`.

## Next Actions (for AI agent)
- Read this PLAN.md.
- Implement all described components using write_file/edit_file for code.
- Fix existing build errors.
- Update/create scripts and docs.
- Run build commands to verify.
