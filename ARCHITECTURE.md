# Neon Flip - Android Architecture Documentation

## Overview

Neon Flip is a native Android arcade game built with **Kotlin** and **Jetpack Compose**, following **MVVM + Clean Architecture** principles. The app features a physics-based gravity-flipping game with user authentication, leaderboard functionality, and backend API integration.

## Technology Stack

| Category | Technology |
|----------|------------|
| **Language** | Kotlin 1.9.20 |
| **UI Framework** | Jetpack Compose |
| **Architecture** | MVVM + Clean Architecture |
| **DI** | Hilt 2.48 |
| **Networking** | Retrofit 2.9.0 + OkHttp 4.12.0 |
| **Async** | Coroutines + Flow |
| **Local Storage** | DataStore Preferences |
| **Navigation** | Compose Navigation |
| **Material** | Material 3 |

## Architecture Layers

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                        │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │  Screens │  │ViewModels│  │  Canvas  │  │  Theme   │   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                      Domain Layer                            │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐                 │
│  │  Models  │  │Use Cases │  │Repositories│               │
│  └──────────┘  └──────────┘  └──────────┘                 │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                       Data Layer                             │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐                 │
│  │   DTOs   │  │   APIs   │  │   Local  │                 │
│  └──────────┘  └──────────┘  └──────────┘                 │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│              Backend API (NestJS + PostgreSQL)              │
└─────────────────────────────────────────────────────────────┘
```

## Project Structure

```
android/
├── app/
│   ├── src/main/
│   │   ├── java/com/neonflip/
│   │   │   ├── presentation/
│   │   │   │   ├── auth/
│   │   │   │   │   ├── LoginScreen.kt
│   │   │   │   │   ├── LoginViewModel.kt
│   │   │   │   │   ├── RegisterScreen.kt
│   │   │   │   │   └── RegisterViewModel.kt
│   │   │   │   ├── game/
│   │   │   │   │   ├── GameScreen.kt
│   │   │   │   │   ├── GameViewModel.kt
│   │   │   │   │   ├── GameEngine.kt
│   │   │   │   │   ├── GameCanvas.kt
│   │   │   │   │   └── GameState.kt
│   │   │   │   ├── leaderboard/
│   │   │   │   │   ├── LeaderboardScreen.kt
│   │   │   │   │   └── LeaderboardViewModel.kt
│   │   │   │   ├── navigation/
│   │   │   │   │   ├── NavGraph.kt
│   │   │   │   │   ├── Route.kt
│   │   │   │   │   └── AuthViewModel.kt
│   │   │   │   ├── theme/
│   │   │   │   │   ├── Color.kt
│   │   │   │   │   ├── Theme.kt
│   │   │   │   │   └── Type.kt
│   │   │   │   └── MainActivity.kt
│   │   │   ├── domain/
│   │   │   │   ├── model/
│   │   │   │   │   ├── User.kt
│   │   │   │   │   ├── Score.kt
│   │   │   │   │   ├── AuthRequest.kt
│   │   │   │   │   ├── AuthResponse.kt
│   │   │   │   │   ├── ApiError.kt
│   │   │   │   │   └── Result.kt
│   │   │   │   ├── repository/
│   │   │   │   │   ├── AuthRepository.kt
│   │   │   │   │   └── ScoreRepository.kt
│   │   │   │   └── usecase/
│   │   │   │       ├── LoginUseCase.kt
│   │   │   │       ├── RegisterUseCase.kt
│   │   │   │       ├── GetCurrentUserUseCase.kt
│   │   │   │       ├── SubmitScoreUseCase.kt
│   │   │   │       └── GetLeaderboardUseCase.kt
│   │   │   ├── data/
│   │   │   │   ├── repository/
│   │   │   │   │   ├── AuthRepositoryImpl.kt
│   │   │   │   │   └── ScoreRepositoryImpl.kt
│   │   │   │   ├── remote/
│   │   │   │   │   ├── api/
│   │   │   │   │   │   ├── AuthApiService.kt
│   │   │   │   │   │   └── ScoreApiService.kt
│   │   │   │   │   ├── dto/
│   │   │   │   │   │   ├── UserDto.kt
│   │   │   │   │   │   ├── LoginRequestDto.kt
│   │   │   │   │   │   ├── RegisterRequestDto.kt
│   │   │   │   │   │   ├── AuthResponseDto.kt
│   │   │   │   │   │   ├── ScoreDto.kt
│   │   │   │   │   │   └── ErrorResponseDto.kt
│   │   │   │   │   └── NetworkInterceptor.kt
│   │   │   │   ├── local/
│   │   │   │   │   └── TokenStorage.kt
│   │   │   │   └── mapper/
│   │   │   │       ├── UserMapper.kt
│   │   │   │       └── ScoreMapper.kt
│   │   │   ├── di/
│   │   │   │   ├── AppModule.kt
│   │   │   │   ├── NetworkModule.kt
│   │   │   │   └── RepositoryModule.kt
│   │   │   └── NeonFlipApplication.kt
│   │   └── res/
│   │       ├── values/
│   │       ├── drawable/
│   │       └── mipmap-*/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
└── build.gradle.kts
```

## Layer Details

### 1. Presentation Layer

**Responsibility**: UI rendering, user interactions, state management

#### Screens (Compose UI)
- **LoginScreen**: Email/password authentication
- **RegisterScreen**: New user registration with validation
- **GameScreen**: Main game with canvas rendering
- **LeaderboardScreen**: Rankings display with LazyColumn

#### ViewModels (State Management)
- **LoginViewModel**: Handles login flow and error states
- **RegisterViewModel**: Handles registration with validation
- **GameViewModel**: Manages game state, physics loop, score submission
- **LeaderboardViewModel**: Fetches and caches leaderboard data
- **AuthViewModel**: Global auth state for navigation

#### Game Components
- **GameEngine**: Physics simulation, collision detection, obstacle spawning
- **GameCanvas**: Custom Canvas drawing for game rendering
- **GameState**: Immutable data class for UI state

#### Navigation
- **NavGraph**: Compose Navigation setup with auth flow
- **Route**: Sealed class defining all navigation routes

#### Theme
- **NeonColor.kt**: Custom neon colors (Cyan, Green, Pink on Dark)
- **Theme.kt**: Material 3 dark theme configuration

### 2. Domain Layer

**Responsibility**: Business logic, use case definitions

#### Models
```kotlin
data class User(val id: String, val username: String, val email: String, val createdAt: String)
data class Score(val id: String, val userId: String, val username: String, val score: Int, val createdAt: String)
sealed class AuthRequest { data class Login(...); data class Register(...) }
data class AuthResponse(val user: User, val accessToken: String, val refreshToken: String)
sealed class Result<out T> { data class Success<T>(val data: T); data class Error(val error: ApiError); object Loading }
```

#### Repository Interfaces
```kotlin
interface AuthRepository {
    suspend fun login(request: AuthRequest.Login): Result<AuthResponse>
    suspend fun register(request: AuthRequest.Register): Result<AuthResponse>
    suspend fun getCurrentUser(): Result<User>
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
}

interface ScoreRepository {
    suspend fun submitScore(score: Int): Result<Score>
    suspend fun getLeaderboard(): Result<List<Score>>
}
```

#### Use Cases
- **LoginUseCase**: Validates input and calls auth repository
- **RegisterUseCase**: Validates input (min password length) and calls auth repository
- **GetCurrentUserUseCase**: Fetches current authenticated user
- **SubmitScoreUseCase**: Submits game score to backend
- **GetLeaderboardUseCase**: Fetches leaderboard rankings

### 3. Data Layer

**Responsibility**: Data persistence, API communication, mappers

#### API Services (Retrofit)
```kotlin
interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<AuthResponseDto>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequestDto): Response<AuthResponseDto>

    @GET("users/me")
    suspend fun getCurrentUser(): Response<UserDto>
}

interface ScoreApiService {
    @POST("scores/submit")
    suspend fun submitScore(@Body request: SubmitScoreRequestDto): Response<SubmitScoreResponseDto>

    @GET("scores/leaderboard")
    suspend fun getLeaderboard(): Response<List<ScoreDto>>
}
```

#### Repository Implementations
- **AuthRepositoryImpl**: Implements AuthRepository with API calls
- **ScoreRepositoryImpl**: Implements ScoreRepository with API calls

#### Local Storage
- **TokenStorage**: DataStore wrapper for secure JWT storage

#### Network
- **AuthInterceptor**: OkHttp interceptor that adds JWT to requests
- **NetworkModule**: Hilt module providing Retrofit & OkHttp

#### DTOs
Separate data transfer objects for API responses, mapped to domain models

### 4. Dependency Injection (Hilt)

#### Modules
- **AppModule**: Provides repositories, use cases, TokenStorage
- **NetworkModule**: Provides Retrofit, OkHttp, API services
- **RepositoryModule**: Binds implementations to interfaces

## Game Physics

### Gravity System
- Player can flip gravity between UP and DOWN by tapping
- Initial velocity boost applied on flip
- Gravity constantly affects vertical velocity

### Obstacles
- Spawn in pairs (top/bottom) with a gap
- Move left at constant speed
- Randomized gap position
- Score increases when passing obstacle

### Collision Detection
```kotlin
fun checkCollision(player: Player, obstacle: Obstacle): Boolean {
    return player.x < obstacle.x + obstacle.width &&
           player.x + player.size > obstacle.x &&
           player.y < obstacle.y + obstacle.height &&
           player.y + player.size > obstacle.y
}
```

### Game Loop
- Runs at ~60 FPS (16ms delay)
- Updates physics, positions, obstacles each frame
- Auto-submits score on game over

## Navigation Flow

```
┌──────────────┐
│   Launch     │
└──────┬───────┘
       ↓
┌──────────────┐    ┌──────────────┐
│   Login      │ ←→ │  Register    │
└──────┬───────┘    └──────────────┘
       ↓ (authenticated)
┌──────────────┐
│    Game      │ ←→ ┌──────────────┐
│              │    │ Leaderboard  │
└──────┬───────┘    └──────────────┘
       ↓ (logout)
┌──────────────┐
│   Login      │
└──────────────┘
```

## API Integration

### Base URL
```
https://unbeguiling-dennis-trispermous.ngrok-free.dev
```

### Endpoints
| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/auth/login` | User login |
| POST | `/auth/register` | User registration |
| GET | `/users/me` | Get current user |
| POST | `/scores/submit` | Submit game score |
| GET | `/scores/leaderboard` | Get top scores |

### JWT Authentication
- Access/refresh tokens stored in DataStore
- AuthInterceptor adds `Authorization: Bearer <token>` header
- Tokens cleared on logout

## Build & Run

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34
- Physical device or emulator with API 26+

### Commands
```bash
# Navigate to project
cd /Users/shubhamkumar/Downloads/Neon-Flip/android

# Clean build
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug

# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

### Gradle Dependencies

```kotlin
// Core
implementation("androidx.core:core-ktx:1.12.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
implementation("androidx.activity:activity-compose:1.8.1")

// Compose BOM
implementation(platform("androidx.compose:compose-bom:2023.10.01"))

// Navigation
implementation("androidx.navigation:navigation-compose:2.7.5")

// Hilt
implementation("com.google.dagger:hilt-android:2.48")
ksp("com.google.dagger:hilt-compiler:2.48")

// Networking
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("com.squareup.okhttp3:okhttp:4.12.0")

// DataStore
implementation("androidx.datastore:datastore-preferences:1.0.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
```

## Key Patterns & Practices

### 1. Unidirectional Data Flow
```kotlin
UI Event → ViewModel → Use Case → Repository → API
         ↓                                           ↓
    UI State ← Result ← Mapper ← DTO ← Response
```

### 2. Sealed Classes for State
```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val error: ApiError) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
```

### 3. Immutable State
```kotlin
data class GameState(
    val score: Int = 0,
    val isGameOver: Boolean = false,
    val isPlaying: Boolean = false,
    val isPaused: Boolean = false,
    val highScore: Int = 0
)
```

### 4. Dependency Injection
All dependencies provided through Hilt modules, testable with fakes/mocks

## Performance Optimizations

1. **Game Loop**: Coroutines with delay for efficient 60 FPS
2. **Canvas Drawing**: Custom Canvas for performant game rendering
3. **LazyColumn**: Efficient leaderboard scrolling
4. **DataStore**: Replaces SharedPreferences for better async performance
5. **Flow**: Reactive state updates in ViewModels

## Security Considerations

1. **Token Storage**: DataStore (encrypted on Android 10+)
2. **HTTPS Only**: No cleartext traffic allowed
3. **Certificate Pinning**: Can be added to OkHttp for production
4. **ProGuard**: Obfuscation enabled in release builds

## Testing Strategy

### Unit Tests
- Use Cases: Test business logic with fake repositories
- ViewModels: Test state management with fake use cases
- Mappers: Test DTO to domain model conversion
- GameEngine: Test physics simulation

### UI Tests
- Compose UI tests for authentication flow
- Game interaction tests (tap to flip gravity)

### Integration Tests
- API integration with mock server
- End-to-end auth flow

## Future Enhancements

1. **Sound Effects**: Background music, flip sounds, collision sounds
2. **Particle Effects**: Trail behind player, explosion on collision
3. **Power-ups**: Shield, slow motion, double points
4. **Difficulty Levels**: Progressive speed increase
5. **Offline Mode**: Local high scores, sync when online
6. **Achievements**: Unlockables, badges
7. **Social**: Friends leaderboard, challenge system
8. **Animations**: Screen transitions, button animations

## Troubleshooting

### Build Issues
```bash
# Clean build
./gradlew clean

# Clear Gradle cache
rm -rf ~/.gradle/caches/

# Invalidate Android Studio caches
File → Invalidate Caches → Invalidate and Restart
```

### Runtime Issues
- Check Logcat for errors
- Verify API URL in NetworkModule
- Ensure device has internet connectivity
- Check DataStore for corrupted tokens

## License

Copyright © 2025 Neon Flip. All rights reserved.
