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

// ── Player surface family — full-screen player & MiniPlayer ──────────────────
internal val Nat_Player_BgTop          = Color(0xFF1A1A2E)  // deep navy  (gradient top)
internal val Nat_Player_BgBottom       = Color(0xFF0E0E14)  // near black (gradient bottom)
internal val Nat_Player_ControlSurface = Color(0xFF252528)  // controls pill background
internal val Nat_Player_PlayBtn        = Color(0xFF4A4A52)  // central play button circle
internal val Nat_MiniPlayer_PillTop    = Color(0xFF3A3A3C)  // mini-player pill gradient top
internal val Nat_MiniPlayer_PillBottom = Color(0xFF1C1C1E)  // mini-player pill gradient bottom

// ── User requested tokens ─────────────────────────────────────────────────────
internal val Nat_Bg_DeepBlack     = Color(0xFF0A0A0A)
internal val Nat_Shimmer_Base     = Color(0xFF1A1A1A)
internal val Nat_Shimmer_Highlight = Color(0xFF2D2D2D)
internal val Nat_Text_Secondary    = Color(0xFFA0A0A0)
internal val Nat_White            = Color(0xFFFFFFFF)

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
    val playerScrim: Color,

    // ── Full-screen player ────────────────────────────────────────────────────
    /** Top color of the full-screen player background gradient (deep navy). */
    val playerBgTop: Color,
    /** Bottom color of the full-screen player background gradient (near black). */
    val playerBgBottom: Color,
    /** Controls pill background in the full-screen player. */
    val playerControlSurface: Color,
    /** Central play/pause button circle background in the full-screen player. */
    val playerPlayBtnSurface: Color,
    /** Inactive seek-track fill colour (white with low opacity). */
    val playerTrackInactive: Color,
    /** Primary text / icon on the dark player background (full white). */
    val onPlayer: Color,
    /** Secondary text / icon on the dark player — subtitles, secondary icons. */
    val onPlayerMuted: Color,
    /** Tertiary text / icon on the dark player — time labels, dim icons. */
    val onPlayerSubtle: Color,

    // ── MiniPlayer ────────────────────────────────────────────────────────────
    /** Top color of the MiniPlayer pill gradient. */
    val miniPlayerPillTop: Color,
    /** Bottom color of the MiniPlayer pill gradient. */
    val miniPlayerPillBottom: Color,

    // ── New semantic tokens ───────────────────────────────────────────────────
    val background: Color,
    val surfaceGlass: Color,
    val surfaceBorder: Color,
    val shimmerBase: Color,
    val shimmerHighlight: Color,
    val textPrimary: Color,
    val textSecondary: Color,
)

// Player tokens are identical for light & dark — the full-screen player and
// MiniPlayer always render on a dark canvas regardless of the system theme.
private val playerTokens = run {
    val white = Color.White
    object {
        val bgTop             = Nat_Player_BgTop
        val bgBottom          = Nat_Player_BgBottom
        val controlSurface    = Nat_Player_ControlSurface
        val playBtnSurface    = Nat_Player_PlayBtn
        val trackInactive     = white.copy(alpha = 0.25f)
        val onPlayer          = white
        val onPlayerMuted     = white.copy(alpha = 0.80f)
        val onPlayerSubtle    = white.copy(alpha = 0.40f)
        val miniPillTop       = Nat_MiniPlayer_PillTop
        val miniPillBottom    = Nat_MiniPlayer_PillBottom
    }
}

private val NatExtendedColorsLight = NatExtendedColors(
    accent                = Nat_Accent,
    accentDim             = Nat_AccentDim,
    waveformActive        = Nat_Purple40,
    waveformInactive      = Nat_Neutral90,
    playerSurface         = Nat_Purple95,
    playerScrim           = Nat_Neutral10.copy(alpha = 0.08f),
    // player
    playerBgTop           = playerTokens.bgTop,
    playerBgBottom        = playerTokens.bgBottom,
    playerControlSurface  = playerTokens.controlSurface,
    playerPlayBtnSurface  = playerTokens.playBtnSurface,
    playerTrackInactive   = playerTokens.trackInactive,
    onPlayer              = playerTokens.onPlayer,
    onPlayerMuted         = playerTokens.onPlayerMuted,
    onPlayerSubtle        = playerTokens.onPlayerSubtle,
    miniPlayerPillTop     = playerTokens.miniPillTop,
    miniPlayerPillBottom  = playerTokens.miniPillBottom,
    // New semantic tokens (Light fallbacks)
    background            = Color.White,
    surfaceGlass          = Color.Black.copy(alpha = 0.05f),
    surfaceBorder         = Color.Black.copy(alpha = 0.1f),
    shimmerBase           = Color(0xFFE0E0E0),
    shimmerHighlight      = Color(0xFFF5F5F5),
    textPrimary           = Color.Black,
    textSecondary         = Color.Gray,
)

private val NatExtendedColorsDark = NatExtendedColors(
    accent                = Nat_Accent,
    accentDim             = Nat_AccentDim,
    waveformActive        = Nat_Purple80,
    waveformInactive      = Nat_Neutral22,
    playerSurface         = Nat_Neutral12,
    playerScrim           = Nat_Neutral0.copy(alpha = 0.40f),
    // player (same dark canvas in both themes)
    playerBgTop           = playerTokens.bgTop,
    playerBgBottom        = playerTokens.bgBottom,
    playerControlSurface  = playerTokens.controlSurface,
    playerPlayBtnSurface  = playerTokens.playBtnSurface,
    playerTrackInactive   = playerTokens.trackInactive,
    onPlayer              = playerTokens.onPlayer,
    onPlayerMuted         = playerTokens.onPlayerMuted,
    onPlayerSubtle        = playerTokens.onPlayerSubtle,
    miniPlayerPillTop     = playerTokens.miniPillTop,
    miniPlayerPillBottom  = playerTokens.miniPillBottom,
    // New semantic tokens (Dark theme)
    background            = Nat_Bg_DeepBlack,
    surfaceGlass          = Color.White.copy(alpha = 0.08f),
    surfaceBorder         = Color.White.copy(alpha = 0.15f),
    shimmerBase           = Nat_Shimmer_Base,
    shimmerHighlight      = Nat_Shimmer_Highlight,
    textPrimary           = Nat_White,
    textSecondary         = Nat_Text_Secondary,
)

/**
 * CompositionLocal that carries [NatExtendedColors] down the tree.
 * staticCompositionLocalOf is used because colors never change within
 * a single theme scope (theme toggle causes full recomposition anyway).
 */
val LocalNatColors = staticCompositionLocalOf { NatExtendedColorsLight }

internal fun natExtendedColors(darkTheme: Boolean) =
    if (darkTheme) NatExtendedColorsDark else NatExtendedColorsLight
