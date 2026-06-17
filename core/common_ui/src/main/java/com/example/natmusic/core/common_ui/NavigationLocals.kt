package com.example.natmusic.core.common_ui

import androidx.compose.runtime.compositionLocalOf
import com.example.natmusic.core.navigation.Navigator

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 *  LocalNavigator — Compose composition local for [Navigator]
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 *  Lives in :core:common_ui so that EVERY feature module can access it without
 *  depending on :app — which would create a circular dependency.
 *
 *  ── Dependency graph (acyclic) ───────────────────────────────────────────────
 *
 *       :core:navigation   (Navigator interface, AppDestination — pure Kotlin)
 *             ▲
 *       :core:common_ui    (LocalNavigator — Compose composition local)  ← here
 *             ▲
 *       :feature:*         (consume LocalNavigator.current in screens)
 *             ▲
 *            :app           (AppNavigatorImpl + provides LocalNavigator)
 *
 *  ── Usage in feature screens ─────────────────────────────────────────────────
 *
 *  ```kotlin
 *  val navigator = LocalNavigator.current
 *
 *  viewModel.singleEvent.collectSingleEvent { event ->
 *      when (event) {
 *          is HomeEffect.NavigateToDetail ->
 *              navigator.navigateTo(AppDestination.Detail(event.id))
 *          HomeEffect.NavigateBack ->
 *              navigator.navigateUp()
 *      }
 *  }
 *  ```
 *
 *  ── Why compositionLocalOf (dynamic) instead of staticCompositionLocalOf? ────
 *
 *  [compositionLocalOf] allows the navigator to be replaced per sub-tree during
 *  instrumented tests without restarting the entire composition.
 *  [staticCompositionLocalOf] would invalidate the entire tree on change.
 *
 *  ── Fail-fast behaviour ──────────────────────────────────────────────────────
 *
 *  Accessing [LocalNavigator.current] outside of [AppNavHost]'s
 *  [CompositionLocalProvider] throws an immediate, descriptive error rather than
 *  silently mis-navigating — intentionally fail-fast.
 */
val LocalNavigator = compositionLocalOf<Navigator> {
    error(
        "LocalNavigator has no value. " +
        "Ensure the calling Composable is placed inside AppNavHost, " +
        "which wraps its content in " +
        "CompositionLocalProvider(LocalNavigator provides navigator) { … }."
    )
}

