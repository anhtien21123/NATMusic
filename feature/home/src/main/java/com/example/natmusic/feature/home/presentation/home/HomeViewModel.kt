package com.example.natmusic.feature.home.home

import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.natmusic.core.mockdata.MockData
import com.example.natmusic.core.mvi.BaseViewModel
import com.example.natmusic.core.service.api.MusicController
import kotlinx.coroutines.launch

class HomeViewModel(
    private val musicController: MusicController
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
            musicController.mediaState.collect { state ->
                updateState { 
                    copy(
                        isPlaying = state.isPlaying,
                        currentMediaId = state.currentMediaItem?.mediaId,
                        playbackProgress = state.progress
                    )
                }
            }
        }
    }

    // ── Intent handling ───────────────────────────────────────────────────────

    override fun handleIntent(intent: HomeContract.Intent) {
        when (intent) {
            is HomeContract.Intent.OpenMusicItem -> startPlayback(intent.id)

            HomeContract.Intent.PlayPause -> {
                if (musicController.mediaState.value.isPlaying) musicController.pause()
                else musicController.resume()
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

            is HomeContract.Intent.Seek -> musicController.seekTo(intent.progress)
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun startPlayback(id: String) {
        val item = MockData.musicList.find { it.id == id } ?: return
        val mediaItem = MediaItem.Builder()
            .setMediaId(item.id)
            .setUri(item.musicUrl)
            .build()
        musicController.play(mediaItem)
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
