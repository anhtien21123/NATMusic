package com.example.natmusic.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.natmusic.core.navigation.Destination
import com.example.natmusic.core.navigation.Navigator
import com.example.natmusic.core.navigation.navigateSingleTop
import com.example.natmusic.core.navigation.popUpTo

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 *  AppNavigatorImpl — production Navigator backed by Nav3-style back-stack
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 *  Implements [Navigator] (from :core:navigation) using a Compose
 *  [SnapshotStateList<Destination>] as the single source of truth for the back-stack.
 *  Every mutation is observed by Compose and triggers an automatic recomposition
 *  of [AppNavHost] — exactly how Nav3's own NavBackStack works internally.
 *
 *  ── Operations ────────────────────────────────────────────────────────────────
 *
 *  navigateTo(R)                    →  backStack.add(R)
 *  navigateUp()                     →  backStack.removeLastOrNull()      (size > 1)
 *  navigateWithClearBackStack(R)    →  backStack.clear(); backStack.add(R)
 *  navigateSingleTop(R)             →  backStack.navigateSingleTop(R)  [ext]
 *  navigateAndPopUp(R, P, i)        →  backStack.popUpTo(i) { it==P }; add(R)
 *
 *  ── Note on LocalNavigator ────────────────────────────────────────────────────
 *
 *  [LocalNavigator] composition local lives in :core:common_ui
 *  (com.example.natmusic.core.common_ui.LocalNavigator) — NOT here — to avoid
 *  circular dependency between :feature:* → :app.
 *
 *  [AppNavHost] imports both this class AND [LocalNavigator] from common_ui,
 *  wrapping screens in:
 *  ```kotlin
 *  CompositionLocalProvider(LocalNavigator provides navigator) { … }
 *  ```
 *
 *  @param backStack The shared [SnapshotStateList] owned by [AppNavHost].
 *                   Must never be empty.
 */
class AppNavigatorImpl(
    private val backStack: SnapshotStateList<Destination>
) : Navigator {

    override fun navigateTo(destination: Destination) {
        backStack.add(destination)
    }

    override fun navigateUp() {
        if (backStack.size > 1) backStack.removeLastOrNull()
    }

    override fun navigateWithClearBackStack(destination: Destination) {
        backStack.clear()
        backStack.add(destination)
    }

    override fun navigateSingleTop(destination: Destination) {
        backStack.navigateSingleTop(destination)
    }

    override fun navigateAndPopUp(
        destination: Destination,
        popUpTo: Destination,
        inclusive: Boolean
    ) {
        backStack.popUpTo(inclusive = inclusive) { it == popUpTo }
        backStack.add(destination)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  rememberAppNavigator — Composable factory
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Creates and caches an [AppNavigatorImpl] tied to [backStack].
 *
 * Stable for the lifetime of the [AppNavHost] composition. Call once at the
 * top of [AppNavHost] and provide via [com.example.natmusic.core.common_ui.LocalNavigator].
 */
@Composable
fun rememberAppNavigator(
    backStack: SnapshotStateList<Destination>
): Navigator = remember(backStack) {
    AppNavigatorImpl(backStack)
}
