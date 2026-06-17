package com.example.natmusic.feature.home.home

import androidx.lifecycle.viewModelScope
import com.example.natmusic.core.mvi.BaseViewModel
import com.example.natmusic.feature.home.domain.model.SkipDirection
import com.example.natmusic.feature.home.domain.usecase.GetHomeFeedUseCase
import com.example.natmusic.feature.home.domain.usecase.GetTrackByIdUseCase
import com.example.natmusic.feature.home.domain.usecase.ObservePlaybackUseCase
import com.example.natmusic.feature.home.domain.usecase.PlayTrackUseCase
import com.example.natmusic.feature.home.domain.usecase.SeekPlaybackUseCase
import com.example.natmusic.feature.home.domain.usecase.SkipTrackUseCase
import com.example.natmusic.feature.home.domain.usecase.TogglePlaybackUseCase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getHomeFeed: GetHomeFeedUseCase,
    private val getTrackById: GetTrackByIdUseCase,
    private val playTrack: PlayTrackUseCase,
    private val observePlayback: ObservePlaybackUseCase,
    private val togglePlayback: TogglePlaybackUseCase,
    private val skipTrack: SkipTrackUseCase,
    private val seekPlayback: SeekPlaybackUseCase
) : BaseViewModel<
    HomeContract.State,
    HomeContract.Intent,
    HomeContract.SingleEvent
>(
    initialState = HomeContract.State()
) {

    init {
        loadFeed()
        observePlayer()
    }

    private fun observePlayer() {
        viewModelScope.launch {
            observePlayback().collect { playback ->
                val track = playback.currentTrackId?.let { getTrackById(it) }
                updateState {
                    copy(
                        isPlaying = playback.isPlaying,
                        currentMediaId = playback.currentTrackId,
                        playbackProgress = playback.progress,
                        nowPlaying = track?.toUi()
                    )
                }
            }
        }
    }

    override fun handleIntent(intent: HomeContract.Intent) {
        when (intent) {
            is HomeContract.Intent.OpenMusicItem -> viewModelScope.launch {
                playTrack(intent.id)
            }

            HomeContract.Intent.PlayPause ->
                togglePlayback(state.value.isPlaying)

            HomeContract.Intent.Next -> viewModelScope.launch {
                skipTrack(SkipDirection.NEXT)
            }

            HomeContract.Intent.Previous -> viewModelScope.launch {
                skipTrack(SkipDirection.PREVIOUS)
            }

            is HomeContract.Intent.Seek -> seekPlayback(intent.progress)
        }
    }

    private fun loadFeed() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            val feed = getHomeFeed()
            updateState {
                copy(
                    isLoading = false,
                    recentItems = feed.recent.map { it.toUi() },
                    recommendedItems = feed.recommended.map { it.toUi() },
                    trendingItems = feed.trending.map { it.toUi() }
                )
            }
        }
    }
}
