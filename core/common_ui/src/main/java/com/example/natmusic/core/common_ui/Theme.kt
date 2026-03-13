package com.example.natmusic.core.common_ui

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// ══════════════════════════════════════════════════════════════════════════════
//  M3 ColorScheme definitions
// ══════════════════════════════════════════════════════════════════════════════

private val NatDarkColorScheme = darkColorScheme(
    primary          = Nat_Purple80,
    onPrimary        = Nat_Purple20,
    primaryContainer = Nat_Purple30,
    onPrimaryContainer = Nat_Purple90,

    secondary        = Nat_Teal80,
    onSecondary      = Nat_Teal20,
    secondaryContainer = Nat_Teal30,
    onSecondaryContainer = Nat_Teal90,

    tertiary         = Nat_Pink80,
    onTertiary       = Nat_Pink20,
    tertiaryContainer = Nat_Pink30,
    onTertiaryContainer = Nat_Pink90,

    error            = Nat_Error80,
    onError          = Nat_Error10,
    errorContainer   = Nat_Error40,       // slightly darker for dark surfaces
    onErrorContainer = Nat_Error90,

    background       = Nat_Bg_DeepBlack,  // Deep Black (#0A0A0A)
    onBackground     = Nat_Neutral100,    // Pure white
    
    surface          = Color(0xFF121212), // Deep surface
    onSurface        = Nat_Neutral100,
    surfaceVariant   = Color(0xFF1E1E1E),
    onSurfaceVariant = Nat_Neutral90,

    outline          = Nat_Neutral22,
    outlineVariant   = Nat_Neutral17,

    scrim            = Nat_Neutral0,
    inverseSurface   = Nat_Neutral90,
    inverseOnSurface = Nat_Neutral10,
    inversePrimary   = Nat_Purple40
)

private val NatLightColorScheme = lightColorScheme(
    primary          = Nat_Purple40,
    onPrimary        = Nat_Purple100,
    primaryContainer = Nat_Purple90,
    onPrimaryContainer = Nat_Purple10,

    secondary        = Nat_Teal40,
    onSecondary      = Nat_Neutral100,
    secondaryContainer = Nat_Teal90,
    onSecondaryContainer = Nat_Teal20,

    tertiary         = Nat_Pink40,
    onTertiary       = Nat_Neutral100,
    tertiaryContainer = Nat_Pink90,
    onTertiaryContainer = Nat_Pink20,

    error            = Nat_Error40,
    onError          = Nat_Neutral100,
    errorContainer   = Nat_Error90,
    onErrorContainer = Nat_Error10,

    background       = Nat_Neutral99,
    onBackground     = Nat_Neutral10,

    surface          = Nat_Neutral95,
    onSurface        = Nat_Neutral10,
    surfaceVariant   = Nat_Purple95,
    onSurfaceVariant = Nat_Neutral10,

    outline          = Nat_Neutral90,
    outlineVariant   = Nat_Purple95,

    scrim            = Nat_Neutral0,
    inverseSurface   = Nat_Neutral10,
    inverseOnSurface = Nat_Neutral95,
    inversePrimary   = Nat_Purple80
)

// ══════════════════════════════════════════════════════════════════════════════
//  MaterialTheme extension accessors
//  These let any composable write `MaterialTheme.natColors` or
//  `MaterialTheme.spacing` exactly like standard M3 tokens.
// ══════════════════════════════════════════════════════════════════════════════

/**
 * NATMusic extended brand colors (accent, waveform, player surface…).
 *
 * ```kotlin
 * Icon(tint = MaterialTheme.natColors.accent)
 * ```
 */
val MaterialTheme.natColors: NatExtendedColors
    @Composable
    @ReadOnlyComposable
    get() = LocalNatColors.current

// ══════════════════════════════════════════════════════════════════════════════
//  NATMusicTheme — single entry-point for the entire design system
//
//  Architecture:
//   :app wraps AppNavHost once → all feature screens inherit the theme.
//   Feature modules in previews wrap individual screens for isolation.
//
//  DFM compatibility:
//   ┌──────────────────────────────────────────────────────────────┐
//   │  :app                                                         │
//   │   └── NATMusicTheme {                                        │
//   │         AppNavHost()                                          │
//   │           ├── :feature:home  (HomeNavScreen)     ← inherits  │
//   │           ├── :feature:login (LoginScreen)       ← inherits  │
//   │           └── :feature:setting (SettingScreen)   ← inherits  │
//   │       }                                                       │
//   └──────────────────────────────────────────────────────────────┘
//
//   CompositionLocals (LocalNatColors, LocalNatSpacing) are resolved through
//   the Compose tree at runtime. A Dynamic Feature Module's Composables that
//   render *inside* NATMusicTheme automatically receive the correct values —
//   there is no need to re-provide them in the feature's own entry point.
//
//   The only rule: each DFM MUST NOT call NATMusicTheme {} internally;
//   instead, receive theme via the slot already established by :app.
//   For standalone DFM previews, wrapping in NATMusicTheme is fine.
// ══════════════════════════════════════════════════════════════════════════════

@Composable
fun NATMusicTheme(
    content: @Composable () -> Unit
) {
    // Force Dark Theme only — ignoring system settings and dynamic colors
    val colorScheme = NatDarkColorScheme

    CompositionLocalProvider(
        LocalNatColors  provides natExtendedColors(darkTheme = true),
        LocalNatSpacing provides NatSpacing(),
        LocalContentColor provides Nat_Neutral100
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography  = NatTypography,
            shapes      = NatShapes,
            content     = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Nat_Bg_DeepBlack) // Ensure base background is always deep black
                ) {
                    content()
                }
            }
        )
    }
}
