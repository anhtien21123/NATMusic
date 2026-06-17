package com.example.natmusic.feature.home.domain.usecase

import com.example.natmusic.feature.home.domain.model.HomeFeed
import com.example.natmusic.feature.home.domain.model.PlaybackState
import com.example.natmusic.feature.home.domain.model.SkipDirection
import com.example.natmusic.feature.home.domain.model.Track
import com.example.natmusic.feature.home.domain.repository.MusicRepository
import com.example.natmusic.feature.home.domain.repository.PlaybackRepository
import kotlinx.coroutines.flow.Flow

class GetHomeFeedUseCase(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(): HomeFeed = musicRepository.getHomeFeed()
}

class GetTrackByIdUseCase(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(trackId: String): Track? = musicRepository.getTrackById(trackId)
}

class ObservePlaybackUseCase(
    private val playbackRepository: PlaybackRepository
) {
    operator fun invoke(): Flow<PlaybackState> = playbackRepository.playbackState
}

class PlayTrackUseCase(
    private val playbackRepository: PlaybackRepository
) {
    suspend operator fun invoke(trackId: String) = playbackRepository.play(trackId)
}

class TogglePlaybackUseCase(
    private val playbackRepository: PlaybackRepository
) {
    operator fun invoke(isCurrentlyPlaying: Boolean) {
        if (isCurrentlyPlaying) playbackRepository.pause()
        else playbackRepository.resume()
    }
}

class SeekPlaybackUseCase(
    private val playbackRepository: PlaybackRepository
) {
    operator fun invoke(progress: Float) = playbackRepository.seekTo(progress)
}

class SkipTrackUseCase(
    private val playbackRepository: PlaybackRepository
) {
    suspend operator fun invoke(direction: SkipDirection) = playbackRepository.skip(direction)
}
