package com.example.natmusic.feature.home.presentation.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.natmusic.core.common_ui.LocalNatColors
import com.example.natmusic.feature.home.NowPlayingUiState

// ── Helpers ───────────────────────────────────────────────────────────────────

private fun formatSeconds(totalSeconds: Int): String {
    val m = totalSeconds / 60
    val s = totalSeconds % 60
    return "%d:%02d".format(m, s)
}

// ─────────────────────────────────────────────────────────────────────────────
// Private sub-components
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Top bar: back arrow ← | "Music Player" title | ••• more options
 */
@Composable
private fun PlayerTopBar(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalNatColors.current
    Row(
        modifier          = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = colors.onPlayer)
        }
        Text(
            text      = "Music Player",
            modifier  = Modifier.weight(1f),
            color     = colors.onPlayer,
            textAlign = TextAlign.Center
        )
        IconButton(onClick = { /* more options */ }) {
            Icon(Icons.Default.MoreHoriz, contentDescription = "More", tint = colors.onPlayer)
        }
    }
}

/**
 * Square album art with rounded corners, filled to the available width.
 */
@Composable
private fun PlayerAlbumArt(
    artworkUrl: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model              = artworkUrl,
        contentDescription = "Album art",
        contentScale       = ContentScale.Crop,
        modifier           = modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(20.dp))
    )
}

/**
 * Bold title + muted subtitle, centred horizontally.
 */
@Composable
private fun PlayerTrackInfo(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    val colors = LocalNatColors.current
    Column(
        modifier            = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text       = title,
            color      = colors.onPlayer,
            fontSize   = 22.sp,
            fontWeight = FontWeight.Bold,
            maxLines   = 1,
            overflow   = TextOverflow.Ellipsis,
            modifier   = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text     = subtitle,
            color    = colors.onPlayerMuted,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Interactive seek bar with a draggable thumb + time labels.
 *
 * ── Seek state (self-contained) ───────────────────────────────────────────────
 *  • [localProgress]  Single source of truth for the rendered position.
 *  • [isUserSeeking]  True while dragging AND while waiting for the Player to
 *                     confirm the seek. Cleared automatically via LaunchedEffect.
 *  • animationSpec    snap() when seeking (zero-lag), tween(300) during playback.
 *
 * @param progress      Current playback position [0.0, 1.0] from the Player.
 * @param totalSeconds  Total track duration in seconds (used for time labels).
 * @param onSeek        Called once on drag-end / tap with the target fraction.
 */
@Composable
private fun PlayerSeekBar(
    progress: Float,
    totalSeconds: Int,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalNatColors.current

    // ── Seek state ────────────────────────────────────────────────────────────
    var localProgress by remember { mutableFloatStateOf(progress) }
    var isUserSeeking by remember { mutableStateOf(false) }

    LaunchedEffect(progress) {
        if (isUserSeeking) {
            if (kotlin.math.abs(progress - localProgress) < 0.05f) isUserSeeking = false
        } else {
            localProgress = progress
        }
    }

    val displayProgress by animateFloatAsState(
        targetValue   = localProgress,
        animationSpec = if (isUserSeeking) snap() else tween(300, easing = LinearEasing),
        label         = "SeekProgress"
    )
    // ─────────────────────────────────────────────────────────────────────────

    Column(modifier = modifier.fillMaxWidth()) {

        // Track + thumb (BoxWithConstraints to get trackWidth as Dp for thumb offset)
        BoxWithConstraints(
            modifier         = Modifier.fillMaxWidth().height(30.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            val trackWidth = maxWidth

            // Invisible full-size touch overlay (pointerInput needs PointerInputScope)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragStart  = { isUserSeeking = true },
                            onDragEnd    = { onSeek(localProgress) },
                            onDragCancel = { isUserSeeking = false }
                        ) { change, _ ->
                            localProgress = (change.position.x / size.width.toFloat()).coerceIn(0f, 1f)
                        }
                    }
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val p = (offset.x / size.width.toFloat()).coerceIn(0f, 1f)
                            localProgress = p
                            onSeek(p)
                        }
                    }
            )

            // ① Inactive track
            Box(Modifier.fillMaxWidth().height(4.dp).clip(CircleShape).background(colors.playerTrackInactive))

            // ② Active fill
            Box(Modifier.fillMaxWidth(displayProgress).height(4.dp).clip(CircleShape).background(colors.onPlayer))

            // ③ Thumb knob
            Box(
                Modifier
                    .padding(start = (trackWidth * displayProgress - 6.dp).coerceAtLeast(0.dp))
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(colors.onPlayer)
            )
        }

        Spacer(Modifier.height(6.dp))

        // Time labels — follow localProgress so they update in real-time while scrubbing
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(formatSeconds((localProgress * totalSeconds).toInt()), color = colors.onPlayerSubtle, fontSize = 12.sp)
            Text(formatSeconds(totalSeconds), color = colors.onPlayerSubtle, fontSize = 12.sp)
        }
    }
}

/**
 * Playback controls row inside a pill-shaped background.
 *
 *  [⇌]  [|◁]  [▶/⏸]  [▷|]  [↺]
 */
@Composable
private fun PlayerControls(
    isPlaying: Boolean,
    onTogglePlay: () -> Unit,
    onSkipPrevious: () -> Unit,
    onSkipNext: () -> Unit,
    onShuffle: () -> Unit,
    onRepeat: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalNatColors.current
    Row(
        modifier              = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50.dp))
            .background(colors.playerControlSurface)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        IconButton(onClick = onShuffle) {
            Icon(Icons.Default.Shuffle, contentDescription = "Shuffle", tint = colors.onPlayerMuted)
        }

        IconButton(onClick = onSkipPrevious) {
            Icon(Icons.Default.SkipPrevious, contentDescription = "Previous", tint = colors.onPlayer, modifier = Modifier.size(32.dp))
        }

        // Central play/pause button
        Box(
            modifier         = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(colors.playerPlayBtnSurface)
                .clickable { onTogglePlay() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Pause" else "Play",
                tint               = colors.onPlayer,
                modifier           = Modifier.size(30.dp)
            )
        }

        IconButton(onClick = onSkipNext) {
            Icon(Icons.Default.SkipNext, contentDescription = "Next", tint = colors.onPlayer, modifier = Modifier.size(32.dp))
        }

        IconButton(onClick = onRepeat) {
            Icon(Icons.Default.Repeat, contentDescription = "Repeat", tint = colors.onPlayerMuted)
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Public screen
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Full-screen, stateless Music Player.
 *
 * ── Layout ────────────────────────────────────────────────────────────────────
 *
 *  ┌──────────────────────────────────────┐
 *  │  PlayerTopBar                        │
 *  │  PlayerAlbumArt                      │
 *  │  PlayerTrackInfo                     │
 *  │  PlayerSeekBar                       │
 *  │  ↕ weight(1f) spacer                 │
 *  │  PlayerControls                      │
 *  └──────────────────────────────────────┘
 *
 * ── Architectural rules ──────────────────────────────────────────────────────
 *  • Pure @Composable — no ViewModel, no Contract.
 *  • All data from [NowPlayingUiState]; all events via named callbacks.
 *  • Seek state is self-contained inside [PlayerSeekBar].
 */
@Composable
fun FullPlayerScreen(
    nowPlaying: NowPlayingUiState,
    onBack: () -> Unit,
    onTogglePlay: () -> Unit,
    onSkipNext: () -> Unit,
    onSkipPrevious: () -> Unit,
    onSeek: (Float) -> Unit,
    onShuffle: () -> Unit,
    onRepeat: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalNatColors.current
    val bgGradient = Brush.verticalGradient(listOf(colors.playerBgTop, colors.playerBgBottom))

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = bgGradient)
    ) {
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PlayerTopBar(onBack = onBack)

            Spacer(Modifier.height(20.dp))

            PlayerAlbumArt(artworkUrl = nowPlaying.artworkUrl)

            Spacer(Modifier.height(28.dp))

            PlayerTrackInfo(
                title    = nowPlaying.title,
                subtitle = nowPlaying.subtitle
            )

            Spacer(Modifier.height(32.dp))

            PlayerSeekBar(
                progress     = nowPlaying.progress,
                totalSeconds = 298,
                onSeek       = onSeek,
                modifier     = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(Modifier.weight(1f))

            PlayerControls(
                isPlaying      = nowPlaying.isPlaying,
                onTogglePlay   = onTogglePlay,
                onSkipPrevious = onSkipPrevious,
                onSkipNext     = onSkipNext,
                onShuffle      = onShuffle,
                onRepeat       = onRepeat,
                modifier       = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF0E0E14, showSystemUi = true)
@Composable
private fun FullPlayerScreenPreview() {
    FullPlayerScreen(
        nowPlaying = NowPlayingUiState(
            trackId    = "1",
            title      = "Crazy in Love",
            subtitle   = "Jon Hickman",
            artworkUrl = "https://picsum.photos/seed/1/400/400",
            progress   = 0.17f,
            isPlaying  = true
        ),
        onBack         = {},
        onTogglePlay   = {},
        onSkipNext     = {},
        onSkipPrevious = {},
        onSeek         = {},
        onShuffle      = {},
        onRepeat       = {}
    )
}
