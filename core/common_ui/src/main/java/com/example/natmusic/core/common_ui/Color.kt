package com.example.natmusic.core.common_ui

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ══════════════════════════════════════════════════════════════════════════════
//  NATMusic Brand Palette — RAW color values
//  Use ONLY via semantic tokens (NatColorScheme / NatExtendedColors),
//  never reference these directly in composables.
// ══════════════════════════════════════════════════════════════════════════════

// ── Purple family (primary) ───────────────────────────────────────────────────
internal val Nat_Purple10  = Color(0xFF21005D)
internal val Nat_Purple20  = Color(0xFF381E72)
internal val Nat_Purple30  = Color(0xFF4F378B)
internal val Nat_Purple40  = Color(0xFF6750A4)   // M3 light primary
internal val Nat_Purple80  = Color(0xFFD0BCFF)   // M3 dark primary
internal val Nat_Purple90  = Color(0xFFEADDFF)
internal val Nat_Purple95  = Color(0xFFF6EDFF)
internal val Nat_Purple100 = Color(0xFFFFFFFF)

// ── Teal family (secondary) ───────────────────────────────────────────────────
internal val Nat_Teal20  = Color(0xFF003733)
internal val Nat_Teal30  = Color(0xFF004F4B)
internal val Nat_Teal40  = Color(0xFF006A64)
internal val Nat_Teal80  = Color(0xFF4FD8CF)
internal val Nat_Teal90  = Color(0xFF71F7EE)

// ── Pink family (tertiary) ────────────────────────────────────────────────────
internal val Nat_Pink20  = Color(0xFF370B1E)
internal val Nat_Pink30  = Color(0xFF561E31)
internal val Nat_Pink40  = Color(0xFF7D5260)
internal val Nat_Pink80  = Color(0xFFEFB8C8)
internal val Nat_Pink90  = Color(0xFFFFD8E4)

// ── Neutral family ────────────────────────────────────────────────────────────
internal val Nat_Neutral0   = Color(0xFF000000)
internal val Nat_Neutral6   = Color(0xFF0F0F0F)
internal val Nat_Neutral10  = Color(0xFF1C1B1F)
internal val Nat_Neutral12  = Color(0xFF1E1E1E)   // Spotify-style dark surface
internal val Nat_Neutral17  = Color(0xFF2B2B2B)
internal val Nat_Neutral22  = Color(0xFF363636)
internal val Nat_Neutral90  = Color(0xFFE6E1E5)
internal val Nat_Neutral95  = Color(0xFFF4EFF4)
internal val Nat_Neutral99  = Color(0xFFFFFBFE)
internal val Nat_Neutral100 = Color(0xFFFFFFFF)

// ── Error family ──────────────────────────────────────────────────────────────
internal val Nat_Error10  = Color(0xFF410002)
internal val Nat_Error40  = Color(0xFFB3261E)
internal val Nat_Error80  = Color(0xFFF2B8B5)
internal val Nat_Error90  = Color(0xFFFFDAD6)

// ── Brand accent — used for playback controls and CTA highlights ──────────────
internal val Nat_Accent     = Color(0xFF1DB954)   // NATMusic green
internal val Nat_AccentDim  = Color(0xFF158A3E)

// ══════════════════════════════════════════════════════════════════════════════
//  NatExtendedColors — custom brand tokens NOT covered by M3's ColorScheme
//
//  Access via:  MaterialTheme.natColors.accent
//  Provided by: NATMusicTheme via LocalNatColors CompositionLocal
//
//  DFM note:
//   CompositionLocals are resolved at runtime through the Compose tree.
//   A Dynamic Feature composable that lives under a NATMusicTheme {} block
//   automatically inherits LocalNatColors — no extra wiring needed.
// ══════════════════════════════════════════════════════════════════════════════

@Immutable
data class NatExtendedColors(
    /** Primary CTA accent (playback, like, key actions). */
    val accent: Color,
    /** Dimmed variant of accent for pressed/disabled states. */
    val accentDim: Color,
    /** Active waveform / equalizer bar. */
    val waveformActive: Color,
    /** Inactive waveform / equalizer bar. */
    val waveformInactive: Color,
    /** Now-playing bar background. */
    val playerSurface: Color,
    /** Scrim behind the bottom player. */
    val playerScrim: Color
)

private val NatExtendedColorsLight = NatExtendedColors(
    accent           = Nat_Accent,
    accentDim        = Nat_AccentDim,
    waveformActive   = Nat_Purple40,
    waveformInactive = Nat_Neutral90,
    playerSurface    = Nat_Purple95,
    playerScrim      = Nat_Neutral10.copy(alpha = 0.08f)
)

private val NatExtendedColorsDark = NatExtendedColors(
    accent           = Nat_Accent,
    accentDim        = Nat_AccentDim,
    waveformActive   = Nat_Purple80,
    waveformInactive = Nat_Neutral22,
    playerSurface    = Nat_Neutral12,
    playerScrim      = Nat_Neutral0.copy(alpha = 0.40f)
)

/**
 * CompositionLocal that carries [NatExtendedColors] down the tree.
 * staticCompositionLocalOf is used because colors never change within
 * a single theme scope (theme toggle causes full recomposition anyway).
 */
val LocalNatColors = staticCompositionLocalOf { NatExtendedColorsLight }

internal fun natExtendedColors(darkTheme: Boolean) =
    if (darkTheme) NatExtendedColorsDark else NatExtendedColorsLight
