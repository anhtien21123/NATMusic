# Project Structure

This project follows Clean Architecture, MVI, Jetpack Compose, Hilt, Retrofit, and Room.

## Directory Tree

```
app/
  ├── src/
  │    └── main/
  │          └── java/
  │                └── com/
  │                      └── example/
  │                            └── natmusic/
  │                                  ├── data/                     
  │                                  │    ├── repository/
  │                                  │    ├── datasource/
  │                                  │    │     ├── remote/
  │                                  │    │     └── local/
  │                                  │    ├── model/
  │                                  │    └── di/
  │                                  │
  │                                  ├── domain/                   
  │                                  │    ├── model/
  │                                  │    ├── repository/
  │                                  │    ├── usecase/
  │                                  │    └── di/
  │                                  │
  │                                  ├── presentation/
  │                                  │    ├── login/                  (Renamed from feature_user)
  │                                  │    │     ├── LoginContract.kt  (UiState, UiIntent, Effect)
  │                                  │    │     ├── LoginViewModel.kt
  │                                  │    │     ├── LoginScreen.kt
  │                                  │    │     └── LoginNavigation.kt
  │                                  │    │
  │                                  │    ├── feature_home/
  │                                  │    │     ├── HomeContract.kt
  │                                  │    │     ├── HomeViewModel.kt
  │                                  │    │     ├── HomeScreen.kt
  │                                  │    │     └── HomeNavigation.kt
  │                                  │    │
  │                                  │    └── common/
  │                                  │          ├── components/
  │                                  │          └── theme/
  │                                  │
  │                                  ├── di/
  │                                  └── MainActivity.kt
```
