package com.example.natmusic.feature.home.data.repository

import androidx.media3.common.MediaItem
import com.example.natmusic.core.service.api.MusicController
import com.example.natmusic.feature.home.domain.model.PlaybackState
import com.example.natmusic.feature.home.domain.model.SkipDirection
import com.example.natmusic.feature.home.domain.repository.MusicRepository
import com.example.natmusic.feature.home.domain.repository.PlaybackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaybackRepositoryImpl(
    private val musicRepository: MusicRepository,
    private val musicController: MusicController
) : PlaybackRepository {

    override val playbackState: Flow<PlaybackState> =
        musicController.mediaState.map { mediaState ->
            PlaybackState(
                isPlaying = mediaState.isPlaying,
                currentTrackId = mediaState.currentMediaItem?.mediaId,
                progress = mediaState.progress
            )
        }

    override suspend fun play(trackId: String) {
        val track = musicRepository.getTrackById(trackId) ?: return
        val mediaItem = MediaItem.Builder()
            .setMediaId(track.id)
            .setUri(track.audioUrl)
            .build()
        musicController.play(mediaItem)
    }

    override fun pause() = musicController.pause()

    override fun resume() = musicController.resume()

    override fun seekTo(progress: Float) = musicController.seekTo(progress)

    override suspend fun skip(direction: SkipDirection) {
        val tracks = musicRepository.getAllTracks()
        if (tracks.isEmpty()) return

        val currentId = musicController.mediaState.value.currentMediaItem?.mediaId
        val currentIndex = tracks.indexOfFirst { it.id == currentId }

        val nextIndex = when (direction) {
            SkipDirection.NEXT -> (currentIndex + 1) % tracks.size
            SkipDirection.PREVIOUS -> if (currentIndex <= 0) tracks.lastIndex else currentIndex - 1
        }
        play(tracks[nextIndex].id)
    }
}
