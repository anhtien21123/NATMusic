package com.example.natmusic.core.common_ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ══════════════════════════════════════════════════════════════════════════════
//  NatSpacing — 8-dp grid spacing system
//
//  Provided via CompositionLocal so any component in the tree can read it
//  without constructor injection.
//
//  Access pattern (inside any @Composable):
//    val spacing = MaterialTheme.spacing
//    Modifier.padding(horizontal = spacing.md, vertical = spacing.sm)
//
//  DFM note:
//   CompositionLocals propagate through the Compose slot tree.
//   A Dynamic Feature screen that sits inside NATMusicTheme {} inherits
//   LocalNatSpacing automatically — identical to the base APK behaviour.
// ══════════════════════════════════════════════════════════════════════════════

@Immutable
data class NatSpacing(
    /** 2 dp — hairline separators */
    val hairline: Dp = 2.dp,
    /** 4 dp — icon internal padding, compact badges */
    val xs: Dp = 4.dp,
    /** 8 dp — inter-item gap in dense lists */
    val sm: Dp = 8.dp,
    /** 12 dp — card internal padding (compact), chip horizontal padding */
    val md12: Dp = 12.dp,
    /** 16 dp — default horizontal screen margin, card padding */
    val md: Dp = 16.dp,
    /** 20 dp — section padding on small screens */
    val lg20: Dp = 20.dp,
    /** 24 dp — section padding, dialog internal padding */
    val lg: Dp = 24.dp,
    /** 32 dp — section divider gap */
    val xl: Dp = 32.dp,
    /** 40 dp — hero section top padding */
    val xl40: Dp = 40.dp,
    /** 48 dp — large section break */
    val xxl: Dp = 48.dp,
    /** 56 dp — bottom navigation height clearance */
    val xxl56: Dp = 56.dp,
    /** 64 dp — now-playing bar height */
    val xxxl: Dp = 64.dp
)

val LocalNatSpacing = staticCompositionLocalOf { NatSpacing() }

/**
 * Convenience accessor — equivalent to LocalNatSpacing.current.
 *
 * ```kotlin
 * Modifier.padding(horizontal = MaterialTheme.spacing.md)
 * ```
 */
val MaterialTheme.spacing: NatSpacing
    @Composable
    @ReadOnlyComposable
    get() = LocalNatSpacing.current
