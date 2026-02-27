package com.example.natmusic.core.common_ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.natmusic.core.common_ui.NATMusicTheme
import com.example.natmusic.core.common_ui.R

// ══════════════════════════════════════════════════════════════════════════════
//  AppTopBar — NATMusic's unified top app bar
//
//  Variants:
//   • AppTopBar (start-aligned) — feature screens with back navigation
//   • AppTopBarCentered         — home / root screens without back action
//
//  Features:
//   • Optional back-navigation icon
//   • Optional subtitle below the title (start-aligned variant only)
//   • Arbitrary trailing actions via [actions] slot
//   • Scroll-linked collapse via [scrollBehavior]
//   • Fully theme-aware — colors from MaterialTheme.colorScheme
//
//  Usage:
//   // Simple back bar
//   AppTopBar(title = "Settings", onNavigateUp = { navBackStack.removeLastOrNull() })
//
//   // With subtitle and action
//   AppTopBar(
//       title    = "Now Playing",
//       subtitle = "NATMusic Playlist",
//       onNavigateUp = { … },
//       actions  = {
//           IconButton(onClick = { … }) { Icon(Icons.Default.MoreVert, null) }
//       }
//   )
//
//   // Centered, no nav icon
//   AppTopBarCentered(title = "Explore")
// ══════════════════════════════════════════════════════════════════════════════

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onNavigateUp: (() -> Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor       = MaterialTheme.colorScheme.surface,
        titleContentColor    = MaterialTheme.colorScheme.onSurface,
        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
        actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ),
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        modifier       = modifier,
        scrollBehavior = scrollBehavior,
        colors         = colors,
        navigationIcon = {
            if (onNavigateUp != null) {
                IconButton(onClick = onNavigateUp) {
                    Icon(
                        imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.nat_cd_navigate_up)
                    )
                }
            }
        },
        title = {
            if (subtitle != null) {
                // Two-line title block
                androidx.compose.foundation.layout.Column {
                    Text(
                        text     = title,
                        style    = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text  = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            } else {
                Text(
                    text     = title,
                    style    = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        actions = actions
    )
}

// ── Centered variant ───────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBarCentered(
    title: String,
    modifier: Modifier = Modifier,
    onNavigateUp: (() -> Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor    = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
        actionIconContentColor     = MaterialTheme.colorScheme.onSurfaceVariant
    ),
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        modifier       = modifier,
        scrollBehavior = scrollBehavior,
        colors         = colors,
        navigationIcon = {
            if (onNavigateUp != null) {
                IconButton(onClick = onNavigateUp) {
                    Icon(
                        imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.nat_cd_navigate_up)
                    )
                }
            }
        },
        title = {
            Text(
                text     = title,
                style    = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        actions = actions
    )
}

// ── Previews ───────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "AppTopBar — with back", showBackground = true)
@Composable
private fun TopBarBackPreview() {
    NATMusicTheme {
        AppTopBar(title = "Settings", onNavigateUp = {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "AppTopBar — subtitle", showBackground = true)
@Composable
private fun TopBarSubtitlePreview() {
    NATMusicTheme {
        AppTopBar(title = "Now Playing", subtitle = "NATMusic Playlist", onNavigateUp = {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "AppTopBarCentered — no back", showBackground = true)
@Composable
private fun TopBarCenteredPreview() {
    NATMusicTheme {
        AppTopBarCentered(title = "Explore")
    }
}

