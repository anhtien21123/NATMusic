package com.example.natmusic.feature.home.home

import com.example.natmusic.core.mockdata.MockMusicItem
import com.example.natmusic.core.mvi.ViewIntent
import com.example.natmusic.core.mvi.ViewSingleEvent
import com.example.natmusic.core.mvi.ViewState

// ── Domain model (move to :domain layer when ready) ───────────────────────────
typealias MusicItem = MockMusicItem

/**
 * MVI contract for the Home feed tab.
 *
 * [HomeViewModel] is the single owner of both feed-catalogue state
 * AND player-observing state. The MiniPlayer in [HomeNavScreen] reads
 * [isPlaying], [currentMediaId], and [playbackProgress] from this state
 * and forwards its callbacks back as explicit [Intent]s to [HomeViewModel].
 */
object HomeContract {

    data class State(
        val isLoading: Boolean = false,
        // ── Feed catalogue ────────────────────────────────────────────────────
        val recentItems: List<MusicItem> = emptyList(),
        val recommendedItems: List<MusicItem> = emptyList(),
        val trendingItems: List<MusicItem> = emptyList(),
        // ── Playback (observed from MusicPlayerHandler) ───────────────────────
        val isPlaying: Boolean = false,
        val currentMediaId: String? = null,
        val playbackProgress: Float = 0f
    ) : ViewState

    sealed class Intent : ViewIntent {
        /** User tapped a song card → start playback. */
        data class OpenMusicItem(val id: String) : Intent()
        /** MiniPlayer play/pause button. */
        data object PlayPause : Intent()
        /** MiniPlayer skip-next button. */
        data object Next : Intent()
        /** MiniPlayer skip-previous button. */
        data object Previous : Intent()
        /** MiniPlayer progress-bar tap → seek to [progress] in [0.0, 1.0]. */
        data class Seek(val progress: Float) : Intent()
    }

    sealed class SingleEvent : ViewSingleEvent {
        data class NavigateToDetail(val id: String) : SingleEvent()
    }
}
