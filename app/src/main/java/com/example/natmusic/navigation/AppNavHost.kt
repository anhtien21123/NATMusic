package com.example.natmusic.navigation

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.natmusic.core.common_ui.LocalNavigator
import com.example.natmusic.core.navigation.AuthDestination
import com.example.natmusic.core.navigation.HomeDestination
import com.example.natmusic.core.navigation.SettingDestination
import com.example.natmusic.core.navigation.Destination
import com.example.natmusic.core.navigation.DeepLinks
import com.example.natmusic.feature.home.navigation.HomeFeatureContent
import com.example.natmusic.feature.login.navigation.AuthFeatureContent
import com.example.natmusic.feature.setting.navigation.SettingFeatureContent

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 *  AppNavHost — Nav3-style top-level navigation host
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 *  This is the SOLE navigation engine for the app.
 *  All screens are registered here; feature modules never see each other.
 *
 *  ── Architecture: Navigator Pattern ──────────────────────────────────────────
 *
 *  Navigation is driven by a UDF side-effect pipeline:
 *
 *    UI  ──[Intent]──►  ViewModel  ──[SingleEvent]──►  UI collects
 *                                                       └─► navigator.navigateTo(...)
 *                                                            └─► backStack mutation
 *                                                                 └─► Compose recompose
 *                                                                      └─► new screen rendered
 *
 *  Key design decisions:
 *  •  [Navigator] interface lives in :core:navigation (no Compose dep).
 *  •  [AppNavigatorImpl] (here in :app) backs [Navigator] with [backStack].
 *  •  [LocalNavigator] provides [Navigator] to the entire composition tree.
 *  •  Feature screens access [LocalNavigator.current] and call navigator
 *     functions directly from [collectSingleEvent] blocks — no navigation
 *     callbacks are threaded back up through the Composable tree.
 *  •  ViewModels NEVER import Navigator. They only emit typed [ViewSingleEvent].
 *
 *  ── Dependency rule (acyclic) ─────────────────────────────────────────────────
 *
 *                    :core:navigation
 *                (AppDestination, MainTab, Navigator)
 *                          ▲
 *          ┌───────────────┼───────────────┐
 *     :feature:home  :feature:login  :feature:setting
 *          └───────────────┼───────────────┘
 *                          ▲
 *                         :app
 *             (AppNavHost + AppNavigatorImpl live here)
 *
 *  ── Back-stack (Nav3-style) ───────────────────────────────────────────────────
 *
 *    • [backStack] is a [SnapshotStateList<AppDestination>] — the single source of truth.
 *    • Every mutation triggers Compose recomposition, rendering the new top entry.
 *    • No NavController, no XML NavGraph, no string routes.
 *
 *  ┌─────────────────────────┬──────────────────────────────────────────────────┐
 *  │  Operation              │  Navigator call                                  │
 *  ├─────────────────────────┼──────────────────────────────────────────────────┤
 *  │  Push screen            │  navigator.navigateTo(AppDestination.Detail(...))      │
 *  │  Go back                │  navigator.navigateUp()                          │
 *  │  Login → Main           │  navigator.navigateAndPopUp(Main, Login, true)   │
 *  │  Single-top tab switch  │  navigator.navigateSingleTop(AppDestination.Main)      │
 *  │  Log-out                │  navigator.navigateWithClearBackStack(Login)     │
 *  └─────────────────────────┴──────────────────────────────────────────────────┘
 *
 *  ── Deep link handling ───────────────────────────────────────────────────────
 *
 *  AndroidManifest.xml (add to MainActivity):
 *  ```xml
 *  <intent-filter android:autoVerify="true">
 *      <action android:name="android.intent.action.VIEW"/>
 *      <category android:name="android.intent.category.DEFAULT"/>
 *      <category android:name="android.intent.category.BROWSABLE"/>
 *      <data android:scheme="natmusic" android:host="natmusic.example.com"/>
 *  </intent-filter>
 *  ```
 *  Supported deep links → resolved back-stacks (see DeepLinks.kt):
 *   natmusic://natmusic.example.com/detail/abc123   → [Login, Main, Detail("abc123")]
 *   natmusic://natmusic.example.com/setting         → [Login, Main, Setting]
 */
@SuppressLint("ContextCastToActivity")
@Composable
fun AppNavHost() {

    // ── Deep link → initial back-stack ───────────────────────────────────────
    val activity = LocalContext.current as? Activity
    val startBackStack: List<Destination> = remember(activity) {
        DeepLinks.resolveBackStack(
            intent = activity?.intent,
            authenticated = false  // swap to `true` after session check
        )
    }

    // ── Back stack (Nav3-style) ───────────────────────────────────────────────
    val backStack = remember {
        mutableStateListOf<Destination>().apply {
            add(startBackStack.first())
        }
    }

    // Push additional deep-link entries (e.g. [Login, Main, Detail("abc")]).
    LaunchedEffect(Unit) {
        startBackStack.drop(1).forEach { route -> backStack.add(route) }
    }

    // ── Navigator — single instance for the full session ─────────────────────
    val navigator = rememberAppNavigator(backStack)

    // ── Provide Navigator to every Composable in the tree ────────────────────
    //
    // Feature screens call `LocalNavigator.current` to get this instance.
    // No navigation lambdas are threaded through the Composable tree.
    CompositionLocalProvider(LocalNavigator provides navigator) {

        // ── Feature-owned dispatch: app only assembles ───────────────────────
        //
        // Each feature handles only its own destination type. By using the shared
        // interfaces, the AppNavHost can render each feature's graph directly without
        // coupling features or needing legacy AppDestination mapping.
        when (val current = backStack.last()) {
            is AuthDestination -> AuthFeatureContent(current, navigator)
            is HomeDestination -> HomeFeatureContent(current, navigator)
            is SettingDestination -> SettingFeatureContent(current, navigator)
            else -> { /* other Destination subtypes from other modules */ }
        }
    }
}
