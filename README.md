# Tiny Grok - Android Client

A lightweight native Android app for chatting with xAI's Grok 4.3 model.

## Features
- Secure API key entry and storage in Settings
- Three themes: Light, Dark, Tokyo Night
- Uses Grok 4.3 via xAI API (`https://api.x.ai/v1/chat/completions`)
- Clean Jetpack Compose UI with MVVM architecture
- Chat with message history (in-memory for now)
- Streaming support planned

## Requirements
- Android Studio or Gradle
- Android SDK 24+
- xAI API key (get from https://x.ai/)

## Building and Running

### Build the APK
```bash
./build.sh
```

### Build and Run in Emulator
```bash
./build.sh --emulate
```
This will build the debug APK, start the emulator (assumes `Pixel_4_API_34` AVD exists), install and launch the app.

Note: If `./gradlew` is missing, the script falls back to `gradle` command. Run `gradle wrapper` first if needed to generate the wrapper.

## Setup
1. Open in Android Studio
2. Sync Gradle
3. In Settings screen, enter your xAI API key (it will be securely stored)
4. Select theme
5. Start chatting!

## Architecture
Follows the plan in PLAN.md: Hilt DI, Retrofit for API, DataStore for settings, Compose for UI.

## License
MIT - For personal/educational use.

## Notes
- Package renamed from com.aicoder to com.tinygrok.client
- API key never logged or exposed
- Tokyo Night theme uses authentic colors from the popular VSCode theme
