package com.example.natmusic.feature.home.home

import com.example.natmusic.core.mvi.ViewIntent
import com.example.natmusic.core.mvi.ViewSingleEvent
import com.example.natmusic.core.mvi.ViewState

// ── Domain model (move to :domain layer when ready) ───────────────────────────
data class MusicItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val imageUrl: String
)

/**
 * MVI contract for the Home (feed) tab.
 * All types implement the corresponding marker interfaces from :core:mvi.
 */
object HomeContract {

    data class State(
        val isLoading: Boolean = false,
        val recentItems: List<MusicItem> = emptyList(),
        val recommendedItems: List<MusicItem> = emptyList(),
        val trendingItems: List<MusicItem> = emptyList()
    ) : ViewState

    sealed class Intent : ViewIntent {
        data class OpenMusicItem(val id: String) : Intent()
    }

    sealed class SingleEvent : ViewSingleEvent {
        data class NavigateToDetail(val id: String) : SingleEvent()
    }
}

