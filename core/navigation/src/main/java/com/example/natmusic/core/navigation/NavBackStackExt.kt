package com.example.natmusic.core.navigation

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 *  NAV BACK-STACK EXTENSIONS  (pure MutableList<T> — no Nav3 runtime dep)
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 *  Nav3's NavBackStack is a SnapshotStateList<T> (which extends MutableList<T>).
 *  These extensions are written against [MutableList] so that:
 *   • :core:navigation compiles without pulling in the nav3-runtime artifact.
 *   • :app calls them on the real NavBackStack<AppDestination> instance.
 *   • Unit tests can drive them with a plain mutableListOf().
 *
 *  ─── Nav2 → Nav3 cheat sheet ─────────────────────────────────────────────────
 *
 *  Nav2  navController.navigate(R)                       → backStack.add(R)
 *  Nav2  navController.popBackStack()                    → backStack.removeLastOrNull()
 *  Nav2  navigate(R) { popUpTo<T> { inclusive = true } } → backStack.popUpTo<T,T>(); add(R)
 *  Nav2  navigate(R) { launchSingleTop = true }          → backStack.navigateSingleTop(R)
 */

/**
 * Removes entries from the TOP of the stack down to (and optionally including)
 * the FIRST entry that satisfies [predicate].
 *
 * If no entry matches, the stack is unchanged.
 *
 * @param inclusive When `true` the matching entry itself is also removed.
 */
fun <T : Any> MutableList<T>.popUpTo(
    inclusive: Boolean = false,
    predicate: (T) -> Boolean
) {
    val targetIndex = indexOfFirst(predicate)
    if (targetIndex < 0) return
    val removeFromIndex = if (inclusive) targetIndex else targetIndex + 1
    subList(removeFromIndex, size).clear()
}

/**
 * Removes entries back to (and optionally including) the first instance of [R].
 *
 * Usage:
 * ```kotlin
 * // After login — remove Login, then push Main:
 * backStack.popUpTo<AppDestination, AppDestination.Login>(inclusive = true)
 * backStack.add(AppDestination.Main)
 * ```
 *
 * Two type parameters are required because [R] must be a subtype of [T]:
 *   T = AppDestination (the list element type / sealed interface)
 *   R = AppDestination.Login (the specific subtype to pop to)
 */
inline fun <T : Any, reified R : T> MutableList<T>.popUpTo(inclusive: Boolean = true) {
    popUpTo(inclusive = inclusive) { it is R }
}

/**
 * Navigate to [destination] ensuring at most one instance exists in the stack
 * (equivalent to Nav2's `launchSingleTop = true`).
 *
 * - Already at top → no-op.
 * - Exists elsewhere → moved to the top.
 * - Not present → added to the top.
 *
 * Usage (tab switching — avoids duplicate tab entries):
 * ```kotlin
 * innerBackStack.navigateSingleTop(MainTab.Explore)
 * ```
 */
fun <T : Any> MutableList<T>.navigateSingleTop(destination: T) {
    if (lastOrNull() == destination) return
    remove(destination)
    add(destination)
}
