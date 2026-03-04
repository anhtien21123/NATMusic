package com.example.natmusic.feature.home.home

import com.example.natmusic.core.mvi.ViewIntent
import com.example.natmusic.core.mvi.ViewSingleEvent
import com.example.natmusic.core.mvi.ViewState

import com.example.natmusic.core.mockdata.MockMusicItem

// ── Domain model (move to :domain layer when ready) ───────────────────────────
typealias MusicItem = MockMusicItem

/**
 * MVI contract for the Home (feed) tab.
 * All types implement the corresponding marker interfaces from :core:mvi.
 */
object HomeContract {

    data class State(
        val isLoading: Boolean = false,
        val recentItems: List<MusicItem> = emptyList(),
        val recommendedItems: List<MusicItem> = emptyList(),
        val trendingItems: List<MusicItem> = emptyList(),
        val isPlaying: Boolean = false,
        val currentMediaId: String? = null
    ) : ViewState

    sealed class Intent : ViewIntent {
        data class OpenMusicItem(val id: String) : Intent()
        object PlayPause : Intent()
        object Next : Intent()
        object Previous : Intent()
    }

    sealed class SingleEvent : ViewSingleEvent {
        data class NavigateToDetail(val id: String) : SingleEvent()
    }
}

