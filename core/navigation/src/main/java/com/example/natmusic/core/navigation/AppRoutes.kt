package com.example.natmusic.core.navigation

import kotlinx.serialization.Serializable

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 *  APP-LEVEL ROUTES  (used by :app's AppNavHost top-level NavDisplay)
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 *  All routes are plain @Serializable objects/classes.
 *  No Nav2 NavGraph, no Nav3 imports — just data types.
 *
 *  Navigation rule (acyclic graph):
 *   :core:navigation ← :feature:* ← :app
 *
 *   Feature modules import these routes to type-check their SingleEvent
 *   destinations (e.g. LoginContract.SingleEvent.GoToMain maps to [AppRoute.Main]).
 *   :app's AppNavHost reads SingleEvents, maps them to [AppRoute], and calls
 *   backStack.add(route). No circular dependency ever forms.
 *
 * ───────────────────────────────────────────────────────────────────────────────
 *  Serialization & deep links
 * ───────────────────────────────────────────────────────────────────────────────
 *  Nav3 uses the route object's identity for in-process navigation (no string
 *  serialisation needed). kotlinx-serialization is used only for:
 *    1. Saved-state restoration across process death.
 *    2. Deep-link URI → route mapping (see [DeepLinks]).
 *
 *  To add a new destination:
 *   1. Add a new nested type below.
 *   2. Add a matching `entry<AppRoute.Foo> { FooScreen(...) }` in AppNavHost.
 *   3. Emit the route from a ViewModel as a SingleEvent.
 */
@Serializable
sealed interface AppRoute {

    /** Unauthenticated entry point. */
    @Serializable
    data object Login : AppRoute

    /**
     * Authenticated shell — wraps the Home/Explore/Library bottom-nav.
     * Internal tab navigation uses [MainRoute]; :app never needs to know
     * which tab is active.
     */
    @Serializable
    data object Main : AppRoute

    /** App-wide settings screen. */
    @Serializable
    data object Setting : AppRoute

    /**
     * Multi-parameter detail screen.
     *
     * [id]     — primary resource identifier (music item, album, playlist…)
     * [origin] — where the navigation came from; used for analytics and
     *            smart back-stack reconstruction on deep links.
     *            e.g. "home", "explore", "deeplink"
     *
     * Deep-link example:  natmusic://detail/abc123?origin=home
     */
    @Serializable
    data class Detail(
        val id: String,
        val origin: String = "unknown"
    ) : AppRoute
}

