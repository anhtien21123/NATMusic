package com.example.natmusic.core.navigation

/**
 * Base type for type-safe navigation. All app destinations implement this.
 * [AppDestination] is the app-level sealed implementation used by the back stack.
 */
interface Destination

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 *  NAVIGATOR — pure navigation abstraction
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 *  Defines every supported navigation operation as an explicit, intention-named
 *  function. The backing implementation ([AppNavigatorImpl] in :app) wires these
 *  to a back-stack of [Destination]; feature modules only see this interface.
 *
 *  ── Dependency rule (acyclic) ─────────────────────────────────────────────────
 *
 *         :core:navigation   (Navigator + AppDestination live here — no Compose dep)
 *               ▲
 *    :feature:* modules      (import Navigator + AppDestination; call navigateTo etc.)
 *               ▲
 *             :app           (AppNavigatorImpl + LocalNavigator live here)
 *
 *  ── Why interface instead of concrete class? ─────────────────────────────────
 *
 *  •  Feature modules can be unit-tested by providing a fake Navigator.
 *  •  :core:navigation stays a pure-Kotlin artifact (zero Compose dependency).
 *  •  Swap the implementation (e.g. real Nav3 NavController) without touching
 *     a single feature screen.
 *
 *  ── Navigation flow (end-to-end) ─────────────────────────────────────────────
 *
 *   UI                  ViewModel              Effect                Navigator
 *   ──────────────────────────────────────────────────────────────────────────
 *   handleIntent(X) ──► handleIntent()  ──►  sendSingleEvent(Nav) ──►  (via
 *   collectSingleEvent           navigateTo / navigateUp / …            backStack)
 *
 *  Feature screens collect [ViewSingleEvent] in [collectSingleEvent], then
 *  call the appropriate [Navigator] function.  The ViewModel NEVER imports
 *  Navigator — it only emits typed [ViewSingleEvent] values.
 */
interface Navigator {

    /**
     * Push [destination] onto the back stack.
     *
     * Nav2 equivalent: `navController.navigate(destination)`
     */
    fun navigateTo(destination: Destination)

    /**
     * Pop the current destination off the back stack.
     * No-op when only one entry remains (prevents empty-stack crash).
     *
     * Nav2 equivalent: `navController.popBackStack()`
     */
    fun navigateUp()

    /**
     * Clear the entire back stack and make [destination] the sole entry.
     * Use for "log out" or "session expired" scenarios.
     *
     * Nav2 equivalent: `navigate(dest) { popUpTo(0) { inclusive = true } }`
     */
    fun navigateWithClearBackStack(destination: Destination)

    /**
     * Navigate to [destination] ensuring at most one instance on the stack.
     *
     * •  Already at top → no-op.
     * •  Exists deeper   → moved to top (existing entry removed first).
     * •  Not present     → added to top.
     *
     * Nav2 equivalent: `navigate(dest) { launchSingleTop = true }`
     */
    fun navigateSingleTop(destination: Destination)

    /**
     * Pop back-stack entries up to (and optionally including) [popUpTo],
     * then push [destination].
     *
     * Example — after successful login:
     * ```kotlin
     * navigator.navigateAndPopUp(
     *     destination = AppDestination.Main,
     *     popUpTo     = AppDestination.Login,
     *     inclusive   = true   // remove Login itself
     * )
     * ```
     *
     * Nav2 equivalent:
     * ```kotlin
     * navController.navigate(destination) {
     *     popUpTo<PopUpTo> { inclusive = inclusive }
     * }
     * ```
     */
    fun navigateAndPopUp(
        destination: Destination,
        popUpTo: Destination,
        inclusive: Boolean = false
    )
}

