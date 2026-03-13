package com.example.natmusic.core.service.api

import androidx.media3.common.MediaItem
import kotlinx.coroutines.flow.StateFlow

/**
 * Single Source of Truth for the media player state.
 */
data class MediaState(
    val isPlaying: Boolean = false,
    val currentMediaItem: MediaItem? = null,
    val progress: Float = 0f, // 0.0 to 1.0
    val duration: Long = 0L,  // in ms
    val currentPosition: Long = 0L // in ms
)

/**
 * Interface that feature modules use to interact with the music service.
 */
interface MusicController {
    val mediaState: StateFlow<MediaState>

    fun play(mediaItem: MediaItem)
    fun pause()
    fun resume()
    fun seekTo(progress: Float)
    fun release()
}
