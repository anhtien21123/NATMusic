package com.example.natmusic.core.navigation



// ── Feature Destination Interfaces (concentrated in this file) ───────────────

/** Interface for Auth feature destinations. */
interface AuthDestination : Destination

/** Interface for Home feature destinations. */
interface HomeDestination : Destination

/** Interface for Setting feature destinations. */
interface SettingDestination : Destination

// ── Navigation Contracts (for cross-module navigation) ───────────────────────

interface AuthNavigationContract {
    val login: AuthDestination
}

interface HomeNavigationContract {
    val main: HomeDestination
    fun detail(id: String, origin: String): HomeDestination
}

interface SettingNavigationContract {
    val setting: SettingDestination
}

/**
 * Tabs inside the main bottom navigation shell.
 * Driven internally by bottom-nav state.
 */
sealed interface MainTab {
    data object Home : MainTab
    data object Explore : MainTab
    data object Library : MainTab
}
