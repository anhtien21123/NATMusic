package com.example.natmusic.core.common_ui

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// ══════════════════════════════════════════════════════════════════════════════
//  NATMusic Shape Tokens
//
//  Follows the Material 3 shape scale — applied to MaterialTheme.shapes so
//  all M3 components (Card, Button, BottomSheet…) automatically pick them up.
//
//  Naming:
//   extraSmall → chips, text fields, small cards
//   small      → buttons, FABs, small dialogs
//   medium     → cards, bottom navigation
//   large      → drawers, large dialogs
//   extraLarge → bottom sheets
//
//  Usage:
//   MaterialTheme.shapes.medium     → standard M3 accessor
//   NatShapes.full                  → for circular shapes not in M3's scale
// ══════════════════════════════════════════════════════════════════════════════

/**
 * M3 shape scale for NATMusic.
 * Pass to [MaterialTheme] so every built-in M3 component inherits these corners.
 */
val NatShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),    // chips, text field corners
    small      = RoundedCornerShape(8.dp),    // small buttons, icon buttons
    medium     = RoundedCornerShape(12.dp),   // cards, album art thumbnails
    large      = RoundedCornerShape(16.dp),   // modal bottom sheet corners
    extraLarge = RoundedCornerShape(28.dp)    // now-playing sheet, full-screen dialogs
)

// ── Supplemental tokens not in M3's Shapes ────────────────────────────────────

/** Fully circular — for avatar images, playback FABs, waveform ticks. */
val NatShapeFull = CircleShape

/** Pill / stadium shape — for filter chips and playback progress pill. */
val NatShapePill = RoundedCornerShape(50)

/** Top-only rounded — for bottom sheets that slide up from an edge. */
val NatShapeTopRounded = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)

