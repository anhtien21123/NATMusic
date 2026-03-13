# Project Structure

This project follows Clean Architecture, MVI, Jetpack Compose, and Koin for Dependency Injection. It is organized into a multi-module architecture to promote scalability and maintainability.

## Module Overview

The project is divided into three main layers: **App**, **Core Modules**, and **Feature Modules**.

### 1. App Shell (`:app`)
The composition root of the application. It depends on all feature and core modules.
- **NATMusicApplication**: Koin initialization and module loading.
- **MainActivity**: The single activity entry point using Nav3 for navigation.
- **DI**: App-level Koin modules and startup configuration.

### 2. Core Modules (`:core`)
Infrastructure and shared logic used across multiple features.
- **:core:mvi**: Base definitions for MVI pattern (ViewState, ViewIntent, ViewSingleEvent).
- **:core:common_ui**: Design system, theme, and reusable UI components (MusicItemCard, etc.).
- **:core:navigation**: Type-safe routes and deep-link constants.
- **:core:service**: Media3 implementation, [MusicService] for background playback, and [MusicPlayerHandler].
- **:core:mockdata**: Centralized mock data for development and testing.

### 3. Feature Modules (`:feature`)
Independent feature modules that contain presentation logic.
- **:feature:home**: Main navigation container (Home, Explore, Library tabs).
- **:feature:login**: Authentication and user login flow.
- **:feature:setting**: Application settings and profile.

## Directory Tree

```
ROOT/
├── app/                  # Application entry point
├── core/                 # Shared infrastructure modules
│   ├── common_ui/        # Design System & Components
│   ├── mockdata/         # Centralized development data
│   ├── mvi/              # MVI base interfaces
│   ├── navigation/       # Type-safe routing logic
│   └── service/          # Media3 Foreground Service & Playback
├── feature/              # Feature modules
│   ├── home/             # Home feed & Bottom navigation tabs
│   ├── login/            # Authentication flow
│   └── setting/          # User settings
└── build.gradle.kts      # Project-level gradle configuration
```

## Technologies Used
- **UI**: Jetpack Compose
- **Architecture**: MVI (Model-View-Intent)
- **Dependency Injection**: Koin
- **Media**: Media3 (ExoPlayer & MediaSession)
- **Navigation**: Jetpack Navigation 3 (Nav3)
- **Networking**: Retrofit (Planned) / Mock Data (Current)
- **Image Loading**: Coil
