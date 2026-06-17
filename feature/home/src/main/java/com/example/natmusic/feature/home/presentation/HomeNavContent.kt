package com.example.natmusic.feature.home.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.natmusic.core.common_ui.spacing
import com.example.natmusic.core.navigation.MainTab
import com.example.natmusic.feature.home.HomeNavContract
import com.example.natmusic.feature.home.NowPlayingUiState
import com.example.natmusic.feature.home.presentation.component.FullPlayerScreen
import com.example.natmusic.feature.home.presentation.component.MiniPlayer

// ── Nav-item descriptor (private) ─────────────────────────────────────────────

private data class NavItem(
    val route: MainTab,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

private val NAV_ITEMS = listOf(
    NavItem(MainTab.Home,    "Home",    Icons.Default.Home,         Icons.Outlined.Home),
    NavItem(MainTab.Explore, "Search",  Icons.Default.Search,       Icons.Outlined.Search),
    NavItem(MainTab.Library, "Library", Icons.Default.LibraryMusic, Icons.Outlined.LibraryMusic)
)

// Height constants used to compute content padding and MiniPlayer positioning.
private val BOTTOM_BAR_RESERVED = 80.dp   // floating pill height (~72dp) + bottom gap
private val MINI_PLAYER_HEIGHT   = 72.dp   // progress strip + content row

/**
 * Pure stateless UI for the authenticated main shell.
 *
 * ── Layout (bottom-up) ────────────────────────────────────────────────────────
 *
 *  ┌──────────────────────────────────────────────────┐
 *  │  Tab content (AnimatedContent)                   │
 *  │  contentPadding.bottom = navBar + miniPlayer     │  ← never obscured
 *  ├──────────────────────────────────────────────────┤
 *  │  MiniPlayer  (AnimatedVisibility, slides up/down)│  ← tappable → full player
 *  ├──────────────────────────────────────────────────┤
 *  │  CustomBottomBar (floating pill)                 │  ← always visible
 *  │  [system navigation bar inset]                   │
 *  ├──────────────────────────────────────────────────┤
 *  │  FullPlayerScreen (slides in from bottom)        │  ← full-screen overlay
 *  └──────────────────────────────────────────────────┘
 *
 * ── Expand / collapse ─────────────────────────────────────────────────────────
 *  [isPlayerExpanded] is local UI state managed here with [remember]. It is
 *  purely presentational — no business logic, no ViewModel needed.
 *
 * @param state                 Tab-selection state from [HomeNavContract].
 * @param nowPlaying            Non-null while a track is loaded; null hides the MiniPlayer.
 * @param onTabSelected         Bottom-nav item tapped.
 * @param onTogglePlay          Play/pause toggle (MiniPlayer + FullPlayer).
 * @param onSkipNext            Skip to next track (FullPlayer).
 * @param onSkipPrevious        Skip to previous track (FullPlayer).
 * @param onFavorite            Heart icon tapped (MiniPlayer).
 * @param onSeek                Progress-bar tapped/dragged; receives [0.0, 1.0].
 * @param homeScreenContent     Slot composable for the Home tab.
 * @param exploreScreenContent  Slot composable for the Explore tab.
 * @param libraryScreenContent  Slot composable for the Library tab.
 */
@Composable
fun HomeNavContent(
    state: HomeNavContract.State,
    nowPlaying: NowPlayingUiState?,
    onTabSelected: (MainTab) -> Unit,
    onTogglePlay: () -> Unit,
    onSkipNext: () -> Unit,
    onSkipPrevious: () -> Unit,
    onFavorite: () -> Unit,
    onSeek: (Float) -> Unit,
    homeScreenContent: @Composable (contentPadding: PaddingValues) -> Unit,
    exploreScreenContent: @Composable (contentPadding: PaddingValues) -> Unit,
    libraryScreenContent: @Composable (contentPadding: PaddingValues) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing      = MaterialTheme.spacing
    val navBarBottom: Dp = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    // Total bottom space each tab screen must leave clear.
    val contentBottomPadding = navBarBottom + BOTTOM_BAR_RESERVED + MINI_PLAYER_HEIGHT + spacing.sm

    // Keep last non-null snapshot so the slide-out animation still has content.
    var lastNowPlaying by remember { mutableStateOf(nowPlaying) }
    if (nowPlaying != null) lastNowPlaying = nowPlaying

    // ── Local UI state: expand / collapse the full-screen player ──────────────
    var isPlayerExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // ── 1. Tab content ─────────────────────────────────────────────────────
        AnimatedContent(
            targetState  = state.currentTab,
            transitionSpec = {
                val from = NAV_ITEMS.indexOfFirst { it.route == initialState }
                val to   = NAV_ITEMS.indexOfFirst { it.route == targetState }
                if (to > from)
                    (slideInHorizontally { it } + fadeIn()).togetherWith(slideOutHorizontally { -it } + fadeOut())
                else
                    (slideInHorizontally { -it } + fadeIn()).togetherWith(slideOutHorizontally { it } + fadeOut())
            },
            label = "TabTransition"
        ) { tab ->
            val padding = PaddingValues(bottom = contentBottomPadding)
            Box(Modifier.fillMaxSize()) {
                when (tab) {
                    MainTab.Home    -> homeScreenContent(padding)
                    MainTab.Explore -> exploreScreenContent(padding)
                    MainTab.Library -> libraryScreenContent(padding)
                }
            }
        }

        // ── 2. MiniPlayer — persistent overlay, directly above the nav bar ────
        AnimatedVisibility(
            visible  = nowPlaying != null,
            enter    = slideInVertically { it } + fadeIn(),
            exit     = slideOutVertically { it } + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(
                    start  = 12.dp,
                    end    = 12.dp,
                    bottom = BOTTOM_BAR_RESERVED
                )
        ) {
            lastNowPlaying?.let { np ->
                MiniPlayer(
                    title = np.title,
                    subtitle = np.subtitle,
                    isPlaying = np.isPlaying,
                    progress = np.progress,
                    onTogglePlay = onTogglePlay,
                    onFavorite = onFavorite,
                    onSeek = onSeek,
                    onClick = { isPlayerExpanded = true }  // ← expand full player
                )
            }
        }

        // ── 3. Floating bottom navigation bar ─────────────────────────────────
        CustomBottomBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = spacing.lg)
                .navigationBarsPadding(),
            currentTab    = state.currentTab,
            onTabSelected = onTabSelected
        )

        // ── 4. Full-screen player overlay (slides up from bottom) ─────────────
        AnimatedVisibility(
            visible  = isPlayerExpanded && nowPlaying != null,
            enter    = slideInVertically { it } + fadeIn(),
            exit     = slideOutVertically { it } + fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            // Use lastNowPlaying so the exit animation still has content when
            // playback stops while the full player is open.
            lastNowPlaying?.let { np ->
                FullPlayerScreen(
                    nowPlaying = np,
                    onBack = { isPlayerExpanded = false },
                    onTogglePlay = onTogglePlay,
                    onSkipNext = onSkipNext,
                    onSkipPrevious = onSkipPrevious,
                    onSeek = onSeek,
                    onShuffle = { /* TODO */ },
                    onRepeat = { /* TODO */ }
                )
            }
        }
    }
}

// ── CustomBottomBar ────────────────────────────────────────────────────────────

@Composable
private fun CustomBottomBar(
    modifier: Modifier = Modifier,
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    val spacing = MaterialTheme.spacing

    Row(
        modifier = modifier
            .padding(horizontal = spacing.lg)
            .height(spacing.xxxl + spacing.sm)
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                shape = CircleShape
            )
            .padding(horizontal = spacing.sm),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        NAV_ITEMS.forEach { item ->
            val isSelected        = currentTab == item.route
            val interactionSource = remember { MutableInteractionSource() }

            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        color  = if (isSelected) MaterialTheme.colorScheme.onBackground
                                 else Color.Transparent,
                        shape  = CircleShape
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication        = null,
                        onClick           = { onTabSelected(item.route) }
                    )
                    .padding(horizontal = spacing.lg20, vertical = spacing.md12),
                verticalAlignment   = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector        = if (isSelected) item.selectedIcon else item.unselectedIcon,
                    contentDescription = item.label,
                    tint               = if (isSelected) MaterialTheme.colorScheme.background
                                         else MaterialTheme.colorScheme.outline,
                    modifier           = Modifier.size(spacing.lg)
                )
                AnimatedVisibility(visible = isSelected) {
                    Row {
                        Spacer(Modifier.width(spacing.sm))
                        Text(
                            text       = item.label,
                            color      = MaterialTheme.colorScheme.background,
                            fontSize   = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(name = "Shell – no playback", showBackground = true)
@Composable
private fun PreviewNoPlayback() {
    MaterialTheme {
        HomeNavContent(
            state                = HomeNavContract.State(currentTab = MainTab.Home),
            nowPlaying           = null,
            onTabSelected        = {},
            onTogglePlay         = {},
            onSkipNext           = {},
            onSkipPrevious       = {},
            onFavorite           = {},
            onSeek               = {},
            homeScreenContent    = { Box(Modifier.fillMaxSize().background(Color.DarkGray)) },
            exploreScreenContent = { Box(Modifier.fillMaxSize().background(Color.DarkGray)) },
            libraryScreenContent = { Box(Modifier.fillMaxSize().background(Color.DarkGray)) }
        )
    }
}

@Preview(name = "Shell – song playing", showBackground = true)
@Composable
private fun PreviewPlaying() {
    MaterialTheme {
        HomeNavContent(
            state      = HomeNavContract.State(currentTab = MainTab.Explore),
            nowPlaying = NowPlayingUiState(
                trackId    = "1",
                title      = "Midnight Rain",
                subtitle   = "Taylor Swift",
                artworkUrl = "https://picsum.photos/seed/1/300/300",
                progress   = 0.45f,
                isPlaying  = true
            ),
            onTabSelected        = {},
            onTogglePlay         = {},
            onSkipNext           = {},
            onSkipPrevious       = {},
            onFavorite           = {},
            onSeek               = {},
            homeScreenContent    = { Box(Modifier.fillMaxSize().background(Color.DarkGray)) },
            exploreScreenContent = { Box(Modifier.fillMaxSize().background(Color.DarkGray)) },
            libraryScreenContent = { Box(Modifier.fillMaxSize().background(Color.DarkGray)) }
        )
    }
}
