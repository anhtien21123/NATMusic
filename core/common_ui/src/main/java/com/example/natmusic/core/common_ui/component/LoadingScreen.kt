package com.example.natmusic.core.common_ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.natmusic.core.common_ui.NATMusicTheme
import com.example.natmusic.core.common_ui.natColors
import com.example.natmusic.core.common_ui.spacing

// ══════════════════════════════════════════════════════════════════════════════
//  LoadingScreen — NATMusic full-screen and overlay loading states
//
//  Variants:
//   • LoadingScreen     — fills entire screen (use as direct route content)
//   • LoadingOverlay    — semi-transparent scrim on top of existing content
//
//  Features:
//   • Optional message below the spinner
//   • Animated fade-in/out via [visible] parameter (works with AnimatedVisibility)
//   • Accent-colored spinner using NATMusic brand color
//   • Fully theme-aware backgrounds
//
//  Usage:
//   // Full-screen placeholder while data is loading
//   if (uiState.isLoading) {
//       LoadingScreen(message = "Loading your library…")
//   }
//
//   // Overlay on top of current content
//   Box(Modifier.fillMaxSize()) {
//       HomeContent(…)
//       LoadingOverlay(visible = uiState.isSubmitting)
//   }
// ══════════════════════════════════════════════════════════════════════════════

/**
 * Full-screen loading state — use as the sole content of a screen while
 * initial data is being fetched.
 *
 * @param message     Optional label below the spinner (e.g. "Loading library…")
 * @param spinnerSize Diameter of the [CircularProgressIndicator].
 */
@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    message: String? = null,
    spinnerSize: Dp = 48.dp
) {
    Box(
        modifier          = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment  = Alignment.Center
    ) {
        LoadingContent(message = message, spinnerSize = spinnerSize)
    }
}

/**
 * Semi-transparent overlay — layer this on top of existing screen content
 * to block interaction while an async operation is in progress.
 *
 * @param visible         Drives [AnimatedVisibility]; fade-in when true.
 * @param backgroundColor Background tint; defaults to [NatExtendedColors.playerScrim].
 * @param message         Optional label below the spinner.
 */
@Composable
fun LoadingOverlay(
    visible: Boolean,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.natColors.playerScrim,
    message: String? = null
) {
    AnimatedVisibility(
        visible = visible,
        enter   = fadeIn(animationSpec  = tween(200)),
        exit    = fadeOut(animationSpec = tween(200)),
        modifier = modifier
    ) {
        Box(
            modifier         = Modifier
                .fillMaxSize()
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            LoadingContent(message = message, spinnerSize = 40.dp)
        }
    }
}

// ── Shared spinner + optional message ─────────────────────────────────────────

@Composable
private fun LoadingContent(
    message: String?,
    spinnerSize: Dp
) {
    // Gently pulse the message text to signal active progress
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val textAlpha by infiniteTransition.animateFloat(
        initialValue   = 1f,
        targetValue    = 0.4f,
        animationSpec  = infiniteRepeatable(
            animation  = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "textAlpha"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier    = Modifier.size(spinnerSize),
            color       = MaterialTheme.natColors.accent,
            strokeWidth = (spinnerSize.value * 0.07f).dp,
            trackColor  = MaterialTheme.colorScheme.surfaceVariant
        )

        if (message != null) {
            Spacer(Modifier.height(MaterialTheme.spacing.md))
            Text(
                text      = message,
                style     = MaterialTheme.typography.bodyMedium,
                color     = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier  = Modifier.alpha(textAlpha)
            )
        }
    }
}

// ── Previews ───────────────────────────────────────────────────────────────────

@Preview(name = "LoadingScreen — no message", showBackground = true)
@Composable
private fun LoadingScreenPreview() {
    NATMusicTheme { LoadingScreen() }
}

@Preview(name = "LoadingScreen — with message", showBackground = true)
@Composable
private fun LoadingScreenMsgPreview() {
    NATMusicTheme { LoadingScreen(message = "Loading your library…") }
}

@Preview(name = "LoadingOverlay — visible", showBackground = true)
@Composable
private fun LoadingOverlayPreview() {
    NATMusicTheme {
        Box(Modifier.fillMaxSize()) {
            Text("Background content", Modifier.align(Alignment.Center))
            LoadingOverlay(visible = true, message = "Saving…")
        }
    }
}

