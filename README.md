# NewsApp

A professional Android news application built with Jetpack Compose, featuring biometric authentication and multiple news source flavors.

## Features

- **Biometric Authentication**: Secure app access with fingerprint/face unlock
- **Pull-to-Refresh**: Modern Material3 refresh interaction
- **Multiple News Sources**: BBC, CNN, and TechCrunch build flavors
- **Rich Article Display**: Full content with images and metadata
- **Browser Integration**: Tap to read full articles in browser

## Setup

1. **Add your News API key** to `local.properties`:
   ```properties
   NEWS_API_KEY=your_api_key_here
   ```
   Get your free API key from [NewsAPI.org](https://newsapi.org/)

2. **Enable biometrics** on your device/emulator for authentication

## Build Flavors

The app includes three news source flavors:

- **BBC**: `./gradlew installBbcDebug`
- **CNN**: `./gradlew installCnnDebug`  
- **TechCrunch**: `./gradlew installTechcrunchDebug`

## Build Commands

```bash
# Build all flavors
./gradlew assembleDebug

# Build specific flavor
./gradlew assembleBbcDebug

# Install and run
./gradlew installBbcDebug
```

## Requirements

- Android SDK 24+
- News API key
- Device with biometric authentication
