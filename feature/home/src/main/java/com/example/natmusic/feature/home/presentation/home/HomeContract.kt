package com.example.natmusic.feature.home.home

import com.example.natmusic.core.mvi.ViewIntent
import com.example.natmusic.core.mvi.ViewSingleEvent
import com.example.natmusic.core.mvi.ViewState

/**
 * MVI contract for the Home feed tab.
 */
object HomeContract {

    data class State(
        val isLoading: Boolean = false,
        val recentItems: List<MusicItemUi> = emptyList(),
        val recommendedItems: List<MusicItemUi> = emptyList(),
        val trendingItems: List<MusicItemUi> = emptyList(),
        val isPlaying: Boolean = false,
        val currentMediaId: String? = null,
        val playbackProgress: Float = 0f,
        val nowPlaying: MusicItemUi? = null
    ) : ViewState

    sealed class Intent : ViewIntent {
        data class OpenMusicItem(val id: String) : Intent()
        data object PlayPause : Intent()
        data object Next : Intent()
        data object Previous : Intent()
        data class Seek(val progress: Float) : Intent()
    }

    sealed class SingleEvent : ViewSingleEvent {
        data class NavigateToDetail(val id: String) : SingleEvent()
    }
}
