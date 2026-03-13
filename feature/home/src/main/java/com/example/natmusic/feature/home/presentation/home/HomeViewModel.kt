package com.example.natmusic.feature.home.home

import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.natmusic.core.mockdata.MockData
import com.example.natmusic.core.mvi.BaseViewModel
import com.example.natmusic.core.service.MusicPlayerHandler
import kotlinx.coroutines.launch

/**
 * ViewModel for the Home feed tab.
 *
 * ── Two responsibilities ──────────────────────────────────────────────────────
 *  1. Load mock feed catalogues (recentItems, recommendedItems, trendingItems).
 *  2. Observe [MusicPlayerHandler] flows and surface the result as playback
 *     fields in [HomeContract.State] so the MiniPlayer in [HomeNavScreen]
 *     can read them without needing a separate ViewModel.
 *
 * ── Why keep playback here and not in a separate ViewModel? ──────────────────
 *  Constraint: no new ViewModels. [HomeViewModel] is already injected in both
 *  [HomeScreen] (via `koinViewModel()`) and [HomeNavScreen] (via `koinViewModel()`).
 *  Because Koin scopes ViewModels to the [ViewModelStore] of the nearest
 *  [ViewModelStoreOwner] (the Activity), both call-sites receive the SAME
 *  instance — so every state update is instantly reflected everywhere.
 *
 * ── Intent routing ────────────────────────────────────────────────────────────
 *  • Feed intents  (OpenMusicItem)          → handled here.
 *  • Player intents (PlayPause/Next/Prev/Seek) → forwarded to [MusicPlayerHandler];
 *    [observePlayer] picks up the resulting state change automatically.
 */
class HomeViewModel(
    private val playerHandler: MusicPlayerHandler
) : BaseViewModel<
    HomeContract.State,
    HomeContract.Intent,
    HomeContract.SingleEvent
>(
    initialState = HomeContract.State()
) {

    init {
        loadMockData()
        observePlayer()
    }

    // ── Player observation ────────────────────────────────────────────────────

    private fun observePlayer() {
        viewModelScope.launch {
            playerHandler.isPlaying.collect { isPlaying ->
                updateState { copy(isPlaying = isPlaying) }
            }
        }
        viewModelScope.launch {
            playerHandler.currentMediaItem.collect { mediaItem ->
                updateState { copy(currentMediaId = mediaItem?.mediaId) }
            }
        }
        viewModelScope.launch {
            playerHandler.progress.collect { progress ->
                updateState { copy(playbackProgress = progress) }
            }
        }
    }

    // ── Intent handling ───────────────────────────────────────────────────────

    override fun handleIntent(intent: HomeContract.Intent) {
        when (intent) {
            is HomeContract.Intent.OpenMusicItem -> startPlayback(intent.id)

            HomeContract.Intent.PlayPause -> {
                if (playerHandler.isPlaying.value) playerHandler.pause()
                else playerHandler.resume()
            }

            HomeContract.Intent.Next -> {
                val currentIndex = MockData.musicList
                    .indexOfFirst { it.id == state.value.currentMediaId }
                val nextIndex = (currentIndex + 1) % MockData.musicList.size
                startPlayback(MockData.musicList[nextIndex].id)
            }

            HomeContract.Intent.Previous -> {
                val currentIndex = MockData.musicList
                    .indexOfFirst { it.id == state.value.currentMediaId }
                val prevIndex = if (currentIndex <= 0) MockData.musicList.size - 1
                               else currentIndex - 1
                startPlayback(MockData.musicList[prevIndex].id)
            }

            is HomeContract.Intent.Seek -> playerHandler.seekTo(intent.progress)
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun startPlayback(id: String) {
        val item = MockData.musicList.find { it.id == id } ?: return
        val mediaItem = MediaItem.Builder()
            .setMediaId(item.id)
            .setUri(item.musicUrl)
            .build()
        playerHandler.play(mediaItem)
    }

    private fun loadMockData() {
        val items = MockData.musicList
        updateState {
            copy(
                recentItems      = items.shuffled(),
                recommendedItems = items.shuffled(),
                trendingItems    = items.shuffled()
            )
        }
    }
}
