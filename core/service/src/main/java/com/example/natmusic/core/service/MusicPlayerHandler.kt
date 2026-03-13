package com.example.natmusic.core.service

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Handler for managing [MediaController] and playback status.
 * This class provides a clean API for UI components to interact with [MusicService].
 */
class MusicPlayerHandler(context: Context) {
    private val sessionToken = SessionToken(context, ComponentName(context, MusicService::class.java))
    private var controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()

    private val controller: MediaController? get() = if (controllerFuture.isDone) controllerFuture.get() else null

    private val _isPlaying = MutableStateFlow(false)
    /** Flow observing [Player.isPlaying] state. */
    val isPlaying = _isPlaying.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    /** Flow observing current playback progress (0.0 to 1.0). */
    val progress = _progress.asStateFlow()

    private val _currentMediaItem = MutableStateFlow<MediaItem?>(null)
    /** Flow observing currently loaded [MediaItem]. */
    val currentMediaItem = _currentMediaItem.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    init {
        controllerFuture.addListener({
            setupPlayerListener()
            startProgressUpdate()
        }, MoreExecutors.directExecutor())
    }

    private fun startProgressUpdate() {
        scope.launch {
            while (isActive) {
                val player = controller
                if (player != null && player.isPlaying && player.duration > 0) {
                    _progress.value = player.currentPosition.toFloat() / player.duration
                }
                delay(1000) // Poll progress every second
            }
        }
    }

    private fun setupPlayerListener() {
        val player = controller ?: return

        // sync initial state
        _isPlaying.value = player.isPlaying
        _currentMediaItem.value = player.currentMediaItem

        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                _currentMediaItem.value = mediaItem
            }
        })
    }

    /**
     * Set a [MediaItem] and start playback.
     */
    fun play(mediaItem: MediaItem) {
        val player = controller ?: return
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    /**
     * Pause the playback.
     */
    fun pause() {
        controller?.pause()
    }

    /**
     * Resume the playback.
     */
    fun resume() {
        controller?.play()
    }

    /**
     * Seek to a specific progress (0.0 to 1.0).
     */
    fun seekTo(progress: Float) {
        val player = controller ?: return
        if (player.duration > 0) {
            val position = (progress * player.duration).toLong()
            player.seekTo(position)
        }
    }

    /**
     * Release the [MediaController]. Should be called when the application is closing
     * or when the handler is no longer needed.
     */
    fun release() {
        MediaController.releaseFuture(controllerFuture)
    }
}
