package com.example.natmusic.core.navigation

import kotlinx.serialization.Serializable

/**
 * ═══════════════════════════════════════════════════════════════════════════════
 *  MAIN-FEATURE ROUTES  (used by :feature:home's internal bottom-nav NavDisplay)
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 *  These routes drive the three tabs inside [HomeNavScreen].
 *  They are defined here (in :core:navigation) rather than inside :feature:home
 *  so that any future feature module can check "are we currently on the Library
 *  tab?" without importing :feature:home's internals.
 *
 *  :app's AppNavHost shows [AppRoute.Main], which renders [HomeNavScreen].
 *  [HomeNavScreen] owns its own Nav3 NavDisplay that navigates among [MainRoute.*].
 *
 *  ┌────────────────────────────────────────────────────────────┐
 *  │  AppNavHost (AppRoute)                                     │
 *  │   └─ AppRoute.Main → HomeNavScreen                        │
 *  │        └─ Internal NavDisplay (MainRoute)                 │
 *  │             ├─ MainRoute.Home    → HomeScreen             │
 *  │             ├─ MainRoute.Explore → ExploreScreen          │
 *  │             └─ MainRoute.Library → LibraryScreen          │
 *  └────────────────────────────────────────────────────────────┘
 */
@Serializable
sealed interface MainRoute {

    @Serializable data object Home    : MainRoute
    @Serializable data object Explore : MainRoute
    @Serializable data object Library : MainRoute
}

