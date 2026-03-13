package com.example.natmusic.core.service.impl

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.natmusic.core.service.api.MediaState
import com.example.natmusic.core.service.api.MusicController
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Implementation of [MusicController] that connects to [MusicService] using [MediaController].
 */
internal class MusicControllerImpl(
    private val context: Context
) : MusicController {

    private val sessionToken = SessionToken(context, ComponentName(context, MusicService::class.java))
    private val controllerFuture: ListenableFuture<MediaController> = 
        MediaController.Builder(context, sessionToken).buildAsync()

    private val controller: MediaController? 
        get() = if (controllerFuture.isDone) controllerFuture.get() else null

    private val _mediaState = MutableStateFlow(MediaState())
    override val mediaState = _mediaState.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    init {
        controllerFuture.addListener({
            setupPlayerListener()
            startProgressUpdate()
        }, MoreExecutors.directExecutor())
    }

    private fun setupPlayerListener() {
        val player = controller ?: return

        // Initial sync
        updateState(player)

        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                updateState(player)
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                updateState(player)
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                updateState(player)
            }
        })
    }

    private fun updateState(player: Player) {
        _mediaState.update { 
            it.copy(
                isPlaying = player.isPlaying,
                currentMediaItem = player.currentMediaItem,
                duration = player.duration.coerceAtLeast(0L),
                currentPosition = player.currentPosition
            )
        }
    }

    private fun startProgressUpdate() {
        scope.launch {
            while (isActive) {
                val player = controller
                if (player != null && player.isPlaying) {
                    _mediaState.update { 
                        it.copy(
                            progress = if (player.duration > 0) player.currentPosition.toFloat() / player.duration else 0f,
                            currentPosition = player.currentPosition
                        )
                    }
                }
                delay(1000)
            }
        }
    }

    override fun play(mediaItem: MediaItem) {
        val player = controller ?: return
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    override fun pause() {
        controller?.pause()
    }

    override fun resume() {
        controller?.play()
    }

    override fun seekTo(progress: Float) {
        val player = controller ?: return
        if (player.duration > 0) {
            val position = (progress * player.duration).toLong()
            player.seekTo(position)
        }
    }

    override fun release() {
        MediaController.releaseFuture(controllerFuture)
    }
}
