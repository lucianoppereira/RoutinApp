# RoutinApp - Wellness Sessions Android App

A modern Android wellness application built with Jetpack Compose that allows users to browse wellness sessions, view detailed information, and manage favorites.

## Features

- **Splash Screen**: Animated entry point with wellness imagery
- **Sessions List**: Browse wellness sessions with search and category filtering
- **Favorites**: Mark and manage favorite sessions with persistent storage
- **Detail View**: Comprehensive session information with instructor details
- **Bottom Navigation**: Easy navigation between Sessions and Favorites
- **Material Design 3**: Modern UI with custom wellness color theme

## Technical Stack

### Core Technologies
- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern declarative UI framework
- **Material Design 3** - UI components and theming system

### Architecture & Dependency Injection
- **Clean Architecture** - Three-layer separation (Domain, Data, Presentation)
- **MVVM Pattern** - Model-View-ViewModel for UI layer
- **Hilt** - Dependency injection framework

### Networking & Data
- **Retrofit** - REST API client with OkHttp
- **SharedPreferences** - Persistent storage for favorites
- **Gson** - JSON serialization/deserialization

### Asynchronous Programming
- **Kotlin Coroutines** - Asynchronous operations
- **StateFlow** - Reactive data streams for UI state

### UI & Navigation
- **Coil** - Image loading and caching
- **Navigation Compose** - Type-safe navigation system

## Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 11 or higher
- Android SDK API level 27 or higher

### Installation & Setup

1. **Clone the repository**
   `ash
   git clone https://github.com/lucianoppereira/routinapp.git
   cd routinapp
   `

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to and select the cloned directory

3. **Sync Gradle files**
   - Android Studio should automatically sync Gradle files
   - If not, click "Sync Project with Gradle Files" from the toolbar

4. **Run the app**
   - Connect an Android device or start an emulator
   - Click the "Run" button (green play icon) in Android Studio
   - Select your target device and click "OK"

### API Configuration

The app currently uses local JSON data for development. To connect to a real API:

1. Update the base URL in NetworkModule.kt
2. Ensure your API returns JSON matching the WellnessSessionDto structure
3. Update the WellnessApiService interface methods as needed

## Screenshots

*(Add screenshots of your app here)*

## License

This project is licensed under the MIT License.
