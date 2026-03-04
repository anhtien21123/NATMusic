package com.example.natmusic.core.service

import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import org.koin.android.ext.android.inject

/**
 * [MediaSessionService] implementation for background playback.
 * 
 * This service handles the [ExoPlayer] and [MediaSession] lifecycle.
 * Media3 automatically manages the foreground service status when playing.
 */
class MusicService : MediaSessionService() {

    private var mediaSession: MediaSession? = null
    
    // We can inject the player if we want to share it, but typically 
    // the service should own the player instance.
    private val player: ExoPlayer by inject()

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        
        // Build the MediaSession
        mediaSession = MediaSession.Builder(this, player)
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
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    /**
     * Custom callback to handle media session events.
     */
    private inner class CustomMediaSessionCallback : MediaSession.Callback {
        // Implement callbacks for custom actions if needed
    }
}
