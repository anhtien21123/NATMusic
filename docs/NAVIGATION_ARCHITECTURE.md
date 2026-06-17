# Feature-owned modular navigation architecture

This document describes the navigation architecture used in NATMusic: **feature-owned destinations**, a **Navigator abstraction**, and **navigation events** so that the app module only assembles graphs and ViewModels never depend on NavController.

---

## 1. Goals

| Goal | How it’s achieved |
|------|--------------------|
| Each feature owns its navigation graph | Feature exposes a single content composable (e.g. `HomeFeatureContent`) that handles its destinations. |
| Each feature defines its own Destination | App-level routes live in `AppDestination` (core); features can add their own sealed types extending `Destination` when adding a new module. |
| App only assembles | AppNavHost uses a single `when (current)` that delegates to feature content composables; it never references individual feature screens. |
| Type-safe navigation | Back stack is `List<Destination>`. Navigator API uses `Destination`, not string routes. |
| ViewModels don’t depend on NavController | ViewModels emit one-shot events (SingleEvent); UI collects and calls `navigator.navigateTo(...)`. |
| Navigation via side effects | SingleEvent (e.g. `NavigateToDetail(id, origin)`) → UI maps to `navigator.navigateTo(AppDestination.Detail(id, origin))`. |
| Navigator abstraction | `Navigator` interface in :core:navigation; :app provides `AppNavigatorImpl` and `LocalNavigator`. |

---

## 2. Recommended package structure

```
app
├── navigation/
│   ├── AppNavHost.kt      # Single when(current) → feature content
│   └── AppNavigator.kt    # rememberAppNavigator, AppNavigatorImpl
└── ...

core/navigation
├── Navigator.kt       # Destination (interface) + Navigator (interface)
├── Destinations.kt   # AppDestination (app back stack) + MainTab (home tabs)
├── NavBackStackExt.kt # popUpTo, navigateSingleTop on MutableList
└── DeepLinks.kt       # intent → List<Destination>

core/common_ui
├── NavigationLocals.kt    # LocalNavigator
└── CollectSingleEvent.kt # collectSingleEvent(Flow, onEvent)

feature/home
├── navigation/
│   └── HomeFeatureContent.kt   # when(route) { Main -> HomeNavScreen(); Detail -> DetailScreen(...) }
├── presentation/
│   ├── HomeNavScreen.kt
│   ├── HomeNavContract.kt
│   └── ...
├── detail/
│   ├── DetailScreen.kt
│   └── DetailContract.kt
└── ...

feature/login
├── navigation/
│   └── AuthFeatureContent.kt   # LoginScreen()
└── ...

feature/setting
├── navigation/
│   └── SettingFeatureContent.kt
└── ...
```

---

## 3. Implementing Destination

**In core-navigation**

- **Base type:** All app destinations implement `Destination` (marker / optional serialization).
- **App-level routes:** A single sealed type is enough for the app back stack. Here it’s `AppDestination` in `AppDestinations.kt`:

```kotlin
// core/navigation
@Serializable
sealed interface AppDestination : Destination {
    @Serializable data object Login : AppDestination
    @Serializable data object Main : AppDestination
    @Serializable data object Setting : AppDestination
    @Serializable data class Detail(val id: String, val origin: String = "unknown") : AppDestination
}
```

**When adding a new feature (e.g. Profile)** you can either:

- **Option A:** Add new variants to `AppDestination` in core (e.g. `Profile(userId)`), then in app add one branch that calls `ProfileFeatureContent(current, navigator)`. No new type in the feature.
- **Option B:** In the new feature module define a sealed interface extending `Destination` and use it in the app’s `when (current)`:

```kotlin
// feature/profile — example when you add the module
@Serializable
sealed interface ProfileDestination : Destination {
    @Serializable data object ProfileHome : ProfileDestination
    @Serializable data class ProfileDetail(val userId: String) : ProfileDestination
}
```

Then in app:

```kotlin
when (val current = backStack.last()) {
    is AppDestination -> when (current) { ... }
    is ProfileDestination -> ProfileFeatureContent(current, navigator)
}
```

---

## 4. How each feature registers its navigation graph

There is no string-based “graph registration”. Each feature **owns a content composable** that knows how to render its destinations:

- **feature-home:** `HomeFeatureContent(current: Destination, navigator: Navigator)`  
  - Handles `AppDestination.Main` → `HomeNavScreen()`, `AppDestination.Detail` → `DetailScreen(id, origin)`.

- **feature-login:** `AuthFeatureContent(navigator)`  
  - Handles Login (app passes this when current is `AppDestination.Login`).

- **feature-setting:** `SettingFeatureContent(navigator)`  
  - Handles `AppDestination.Setting` → `SettingScreen()`.

To “register” a new destination for an existing feature:

1. Add the route to `AppDestination` in core (if app-level).
2. In the feature’s content composable, add a `when` branch and the screen. The app’s `when` only needs to keep delegating that route to the same feature content (e.g. still `AppDestination.Main, is AppDestination.Detail -> HomeFeatureContent(...)`).

---

## 5. How the app composes all feature graphs

**AppNavHost** holds the single back stack and dispatches by destination:

```kotlin
// app/navigation/AppNavHost.kt (simplified)
val backStack = remember { mutableStateListOf<Destination>().apply { add(startBackStack.first()) } }
val navigator = rememberAppNavigator(backStack)

CompositionLocalProvider(LocalNavigator provides navigator) {
    when (val current = backStack.last()) {
        is AppDestination -> when (current) {
            AppDestination.Login -> AuthFeatureContent(navigator)
            AppDestination.Main, is AppDestination.Detail -> HomeFeatureContent(current, navigator)
            AppDestination.Setting -> SettingFeatureContent(navigator)
        }
        // Future: is ProfileDestination -> ProfileFeatureContent(current, navigator)
        else -> { }
    }
}
```

So the app:

- Owns the back stack (`List<Destination>`).
- Provides `Navigator` via `LocalNavigator`.
- Does a single `when (current)` and delegates to feature content; it does **not** reference `HomeNavScreen`, `DetailScreen`, `LoginScreen`, etc. directly.

---

## 6. Navigation event flow (ViewModel → UI → Navigator)

1. **User action** (e.g. tap “Track” or “Back”) → UI calls `viewModel.handleIntent(Intent.OnDetailRequested(id, origin))` or `Intent.OnBackClicked`.
2. **ViewModel** does business logic and emits a one-shot effect:  
   `sendSingleEvent(HomeNavContract.SingleEvent.NavigateToDetail(id, origin))` or `NavigateBack`.
3. **UI** collects with `viewModel.singleEvent.collectSingleEvent { event -> ... }` and maps to Navigator:
   - `NavigateToDetail(id, origin)` → `navigator.navigateTo(AppDestination.Detail(id, origin))`
   - `NavigateBack` / `NavigateToMain` etc. → `navigator.navigateUp()` or `navigator.navigateAndPopUp(...)`.
4. **Navigator** (e.g. `AppNavigatorImpl`) mutates the back stack; Compose recomposes and the next screen is shown.

ViewModels never receive or use `NavController` or `Navigator`; they only emit contract SingleEvents. All navigation calls happen in the UI layer using `LocalNavigator.current`.

---

## 7. Example: Home and Profile (type-safe, no string routes)

**Home (existing)**  
- **Destinations:** `AppDestination.Main`, `AppDestination.Detail(id, origin)`.  
- **ViewModel:**  
  `sendSingleEvent(NavigateToDetail(id, origin))` or `NavigateToSettings`.  
- **UI:**  
  `collectSingleEvent { when (it) { is NavigateToDetail -> navigator.navigateTo(AppDestination.Detail(it.id, it.origin)); NavigateToSettings -> navigator.navigateTo(AppDestination.Setting) } }`.

**Profile (example for a new feature)**  
- **Destinations:** e.g. `ProfileDestination.ProfileHome`, `ProfileDestination.ProfileDetail(userId)` (if you use Option B above), or `AppDestination.Profile(userId)` if you extend `AppDestination`.  
- **ViewModel:**  
  `sendSingleEvent(ProfileContract.SingleEvent.NavigateToProfileDetail(userId))`.  
- **UI:**  
  `navigator.navigateTo(ProfileDestination.ProfileDetail(userId))` or `navigator.navigateTo(AppDestination.Profile(userId))`.  
- **App:**  
  Add `is ProfileDestination -> ProfileFeatureContent(current, navigator)` (or an `AppDestination.Profile` branch that calls `ProfileFeatureContent`).

No string routes are used in ViewModels or in the Navigator API; only typed `Destination` (and optionally serialization for process death / deep links).

---

## 8. Navigator API (recap)

```kotlin
interface Navigator {
    fun navigateTo(destination: Destination)
    fun navigateUp()
    fun navigateWithClearBackStack(destination: Destination)
    fun navigateSingleTop(destination: Destination)
    fun navigateAndPopUp(destination: Destination, popUpTo: Destination, inclusive: Boolean = false)
}
```

Implemented in :app as `AppNavigatorImpl(backStack: SnapshotStateList<Destination>)`, provided via `LocalNavigator`.

---

## 9. Why this scales

- **Single back stack of `Destination`** keeps one source of truth and makes it easy to add new destination types (e.g. Profile) without touching other features.
- **Feature content composables** keep “which screen for this destination” inside the feature; the app only does “which feature for this destination”.
- **Navigator abstraction** allows tests to use a fake Navigator and keeps Compose/NavController out of core and ViewModels.
- **SingleEvent → Navigator in UI** keeps navigation a side effect and preserves a clear unidirectional flow.
