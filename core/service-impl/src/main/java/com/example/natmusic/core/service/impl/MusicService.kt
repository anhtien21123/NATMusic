package com.example.natmusic.core.service.impl

import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

/**
 * [MediaSessionService] implementation for background playback.
 * 
 * OWNERSHIP: This service owns the [ExoPlayer] instance directly to manage its lifecycle
 * correctly (creation in onCreate, release in onDestroy). It does NOT inject the player.
 */
class MusicService : MediaSessionService() {

    private var mediaSession: MediaSession? = null
    private var player: ExoPlayer? = null

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        
        // 1. Create the Player instance
        player = ExoPlayer.Builder(this)
            .build()
        
        // 2. Build the MediaSession
        mediaSession = MediaSession.Builder(this, player!!)
            .setCallback(CustomMediaSessionCallback())
            .build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    /**
     * Stop the service when the task is removed from recents if not playing.
     */
    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession?.player ?: return
        if (!player.playWhenReady || player.mediaItemCount == 0) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release() // Player is released here along with the session
            release()
            mediaSession = null
        }
        player = null
        super.onDestroy()
    }

    /**
     * Custom callback for media session events.
     */
    private inner class CustomMediaSessionCallback : MediaSession.Callback {
        // Implement callbacks for custom actions here
    }
}
