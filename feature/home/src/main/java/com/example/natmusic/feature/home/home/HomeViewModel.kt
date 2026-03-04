package com.example.natmusic.feature.home.home

import androidx.lifecycle.viewModelScope
import com.example.natmusic.core.mvi.BaseViewModel

import com.example.natmusic.core.mockdata.MockData
import com.example.natmusic.core.service.MusicPlayerHandler
import androidx.media3.common.MediaItem
import kotlinx.coroutines.launch

/**
 * Home-feed ViewModel.
 *
 * Extends [BaseViewModel] with typed [ViewState], [ViewIntent], [ViewSingleEvent].
 * Koin registers it via `viewModelOf(::HomeViewModel)` — zero annotations required.
 *
 * Adding a repository dependency is automatic:
 *   class HomeViewModel(private val repo: MusicRepository) : BaseViewModel<...>
 *   → Koin resolves MusicRepository via the module that binds it.
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
    }

    override fun handleIntent(intent: HomeContract.Intent) {
        when (intent) {
            is HomeContract.Intent.OpenMusicItem -> {
                val item = MockData.musicList.find { it.id == intent.id } ?: return
                val mediaItem = MediaItem.Builder()
                    .setMediaId(item.id)
                    .setUri(item.musicUrl)
                    .build()
                playerHandler.play(mediaItem)
            }
            HomeContract.Intent.PlayPause -> {
                if (playerHandler.isPlaying.value) playerHandler.pause() 
                else playerHandler.resume()
            }
            HomeContract.Intent.Next -> {
                // Simplified next logic using mock list
                val currentIndex = MockData.musicList.indexOfFirst { it.id == state.value.currentMediaId }
                val nextIndex = (currentIndex + 1) % MockData.musicList.size
                handleIntent(HomeContract.Intent.OpenMusicItem(MockData.musicList[nextIndex].id))
            }
            HomeContract.Intent.Previous -> {
                val currentIndex = MockData.musicList.indexOfFirst { it.id == state.value.currentMediaId }
                val prevIndex = if (currentIndex <= 0) MockData.musicList.size - 1 else currentIndex - 1
                handleIntent(HomeContract.Intent.OpenMusicItem(MockData.musicList[prevIndex].id))
            }
        }
    }

    private fun loadMockData() {
        val items = MockData.musicList
        updateState {
            copy(
                recentItems = items.shuffled(),
                recommendedItems = items.shuffled(),
                trendingItems = items.shuffled()
            )
        }
    }
}

