package com.example.natmusic.feature.home.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.natmusic.core.common_ui.LocalNatColors

// ── Design tokens ──────────────────────────────────────────────────────────────

private val PillShape = RoundedCornerShape(percent = 50)

// ── PlaybackProgressBar ────────────────────────────────────────────────────────

/**
 * Canvas-only, thumb-less progress bar.
 *
 * Draws two rectangles — [inactiveColor] (full width) then [activeColor]
 * (progress-clipped). No knob, no Slider dependency.
 * A tap anywhere fires [onSeek] with the normalised position [0.0, 1.0].
 */
@Composable
fun PlaybackProgressBar(
    progress: Float,
    activeColor: Color,
    inactiveColor: Color,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
    barHeight: Dp = 2.dp
) {
    Canvas(
        modifier = modifier.pointerInput(onSeek) {
            detectTapGestures { offset ->
                onSeek((offset.x / size.width.toFloat()).coerceIn(0f, 1f))
            }
        }
    ) {
        // ① full inactive track
        drawRect(color = inactiveColor, size = size.copy(height = barHeight.toPx()))
        // ② elapsed active fill — no thumb
        drawRect(
            color = activeColor,
            size = size.copy(
                width = size.width * progress.coerceIn(0f, 1f),
                height = barHeight.toPx()
            )
        )
    }
}

// ── CircularProgressRing ───────────────────────────────────────────────────────

/**
 * Draws a circular progress arc around the play button.
 *
 * - Full ring in [inactiveColor] (always visible).
 * - Partial arc in [activeColor] sweeping clockwise from the top (-90°),
 *   proportional to [progress] ∈ [0.0, 1.0].
 * - [strokeWidth] controls how thick the ring is.
 */
@Composable
private fun CircularProgressRing(
    progress: Float,
    activeColor: Color,
    inactiveColor: Color,
    strokeWidth: Dp = 3.dp,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val stroke = strokeWidth.toPx()
        val inset = stroke / 2f
        val arcSize = Size(size.width - stroke, size.height - stroke)
        val topLeft = Offset(inset, inset)

        // ① full inactive ring
        drawArc(
            color = inactiveColor,
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )

        // ② active progress arc (only drawn when there is meaningful progress)
        if (progress > 0f) {
            drawArc(
                color = activeColor,
                startAngle = -90f,
                sweepAngle = 360f * progress.coerceIn(0f, 1f),
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }
    }
}

// ── MiniPlayer ────────────────────────────────────────────────────────────────

/**
 * Persistent, stateless MiniPlayer — pill/capsule design.
 *
 * ── Layout (left → right) ─────────────────────────────────────────────────────
 *
 *  ╔══════════════════════════════════════════════════════╗
 *  ║  ◉  │  Title (bold)                          │  ♡   ║
 *  ║ ring │  Subtitle                              │ Fav  ║
 *  ╚══════════════════════════════════════════════════════╝
 *    ↑ progress ring wraps the circular play/pause button
 *
 * ── Statelessness ─────────────────────────────────────────────────────────────
 *  Pure @Composable. No ViewModel, no Contract type.
 *  All data is flat primitives; all gestures are explicit named callbacks.
 *
 * @param title         Track title shown in bold white.
 * @param subtitle      Artist / label shown in muted white.
 * @param isPlaying     True → Pause icon; false → Play icon.
 * @param progress      Playback position [0.0, 1.0] for the circular ring.
 * @param onTogglePlay  Circular play/pause button tapped.
 * @param onFavorite    Heart icon tapped.
 * @param onSeek        Reserved for seek events (e.g. future long-press scrub).
 * @param onClick       Tapping anywhere outside the icon buttons expands the full player.
 */
@Composable
fun MiniPlayer(
    title: String,
    subtitle: String,
    isPlaying: Boolean,
    progress: Float,
    onTogglePlay: () -> Unit,
    onFavorite: () -> Unit,
    onSeek: (Float) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors       = LocalNatColors.current
    val pillGradient = Brush.verticalGradient(listOf(colors.miniPlayerPillTop, colors.miniPlayerPillBottom))

    // Ripple-free clickable so the pill expands on tap outside buttons
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = PillShape,
                ambientColor = Color.Black.copy(alpha = 0.6f),
                spotColor = Color.Black.copy(alpha = 0.6f)
            )
            .clip(PillShape)
            .background(brush = pillGradient)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ① Circular play/pause button wrapped in a progress ring ──────────
            Box(
                modifier = Modifier.size(58.dp), // ring container (slightly larger than button)
                contentAlignment = Alignment.Center
            ) {
                // Progress ring drawn behind the button
                CircularProgressRing(
                    progress      = progress,
                    activeColor   = colors.onPlayer.copy(alpha = 0.9f),
                    inactiveColor = colors.onPlayer.copy(alpha = 0.18f),
                    strokeWidth   = 3.dp,
                    modifier      = Modifier.fillMaxSize()
                )

                // Black circular button on top of the ring
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.Black)
                        .pointerInput(onTogglePlay) {
                            detectTapGestures { onTogglePlay() }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector        = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint               = colors.onPlayer,
                        modifier           = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            // ② Title + subtitle ──────────────────────────────────────────────
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = title,
                    color      = colors.onPlayer,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text     = subtitle,
                    color    = colors.onPlayerMuted,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // ③ Favorite button ───────────────────────────────────────────────
            IconButton(onClick = onFavorite) {
                Icon(
                    imageVector        = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Add to favorites",
                    tint               = colors.onPlayer,
                    modifier           = Modifier.size(22.dp)
                )
            }
        }
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(name = "MiniPlayer – Playing", showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun MiniPlayerPlayingPreview() {
    MiniPlayer(
        title = "Label Podcast Name Here",
        subtitle = "Record Label",
        isPlaying = true,
        progress = 0.38f,
        onTogglePlay = {},
        onFavorite = {},
        onSeek = {},
        onClick = {},
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Preview(name = "MiniPlayer – Paused", showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun MiniPlayerPausedPreview() {
    MiniPlayer(
        title = "As It Was",
        subtitle = "Harry Styles",
        isPlaying = false,
        progress = 0.72f,
        onTogglePlay = {},
        onFavorite = {},
        onSeek = {},
        onClick = {},
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}
