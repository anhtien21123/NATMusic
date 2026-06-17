package com.example.natmusic.feature.home

import com.example.natmusic.core.mvi.ViewIntent
import com.example.natmusic.core.mvi.ViewSingleEvent
import com.example.natmusic.core.mvi.ViewState
import com.example.natmusic.core.navigation.MainTab

/**
 * Lightweight snapshot of the currently playing track.
 *
 * Derived inside [HomeNavScreen] from [HomeContract.State] and passed down to
 * [HomeNavContent] → [MiniPlayer]. Using a plain data class (not a nested
 * contract type) keeps [MiniPlayer] free of any ViewModel/Contract imports.
 *
 * Null when nothing is queued in the player.
 */
data class NowPlayingUiState(
    val trackId: String,
    val title: String,
    val subtitle: String,
    val artworkUrl: String,
    val progress: Float,
    val isPlaying: Boolean
)

/**
 * MVI contract for the home-container (bottom-nav shell).
 *
 * [MainTab] is imported from :core:navigation so that any future feature
 * module can reference the tab routes without depending on :feature:home.
 *
 * Navigation effects bubble up as [SingleEvent] to :app's AppNavHost, which
 * maps them to the correct [AppDestination] navigation without :feature:home ever
 * importing AppDestination or NavController.
 */
object HomeNavContract {

    data class State(
        val currentTab: MainTab = MainTab.Home
    ) : ViewState

    sealed class Intent : ViewIntent {
        data class OnTabSelected(val route: MainTab) : Intent()
    }

    sealed class SingleEvent : ViewSingleEvent
}
