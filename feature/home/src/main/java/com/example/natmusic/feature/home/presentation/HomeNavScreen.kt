package com.example.natmusic.feature.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.natmusic.core.common_ui.collectSingleEvent
import com.example.natmusic.core.mockdata.MockData
import com.example.natmusic.feature.home.HomeNavContract
import com.example.natmusic.feature.home.HomeNavViewModel
import com.example.natmusic.feature.home.NowPlayingUiState
import com.example.natmusic.feature.home.explore.ExploreScreen
import com.example.natmusic.feature.home.home.HomeContract
import com.example.natmusic.feature.home.home.HomeScreen
import com.example.natmusic.feature.home.home.HomeViewModel
import com.example.natmusic.feature.home.library.LibraryScreen
import org.koin.androidx.compose.koinViewModel

/**
 * Stateful shell for the authenticated main graph.
 *
 * ── Two ViewModels, one shell ─────────────────────────────────────────────────
 *  • [HomeNavViewModel]  — owns tab-selection state ([HomeNavContract.State]).
 *  • [HomeViewModel]     — owns feed catalogues AND real-time playback state
 *                          ([HomeContract.State.isPlaying], [currentMediaId],
 *                          [playbackProgress]).
 *
 *  Both are resolved via `koinViewModel()`. Because Koin scopes ViewModels to
 *  the nearest [ViewModelStoreOwner] (the Activity), [HomeViewModel] here and
 *  inside [HomeScreen] are the SAME instance — state changes propagate instantly.
 *
 * ── MiniPlayer wiring ─────────────────────────────────────────────────────────
 *  1. [homeState.currentMediaId] is resolved against [MockData.musicList] to
 *     obtain a [NowPlayingUiState] (title, subtitle, artwork, progress, isPlaying).
 *  2. This snapshot is passed to [HomeNavContent] → [com.example.natmusic.feature.home.presentation.component.MiniPlayer].
 *  3. MiniPlayer callbacks are mapped here to explicit [HomeContract.Intent]s
 *     and forwarded to [homeViewModel.handleIntent].
 *
 * ── Side-effects ──────────────────────────────────────────────────────────────
 *  Navigation events from [HomeNavViewModel] (Settings, Detail) are collected
 *  in [collectSingleEvent] and forwarded to [AppNavHost] via lambda params.
 */
@Composable
fun HomeNavScreen(
    viewModel: HomeNavViewModel = koinViewModel(),
    homeViewModel: HomeViewModel = koinViewModel(),
    onNavigateToSetting: () -> Unit,
    onNavigateToDetail: (id: String, origin: String) -> Unit
) {
    val state     by viewModel.state.collectAsStateWithLifecycle()
    val homeState by homeViewModel.state.collectAsStateWithLifecycle()

    // ── Side-effects: navigation ───────────────────────────────────────────────
    viewModel.singleEvent.collectSingleEvent { event ->
        when (event) {
            HomeNavContract.SingleEvent.NavigateToSettings ->
                onNavigateToSetting()
            is HomeNavContract.SingleEvent.NavigateToDetail ->
                onNavigateToDetail(event.id, event.origin)
        }
    }

    // ── Derive MiniPlayer state from HomeViewModel ─────────────────────────────
    //
    // Inline derivation (no remember needed — Compose will skip recomposition
    // when the result is structurally equal to the previous value because
    // NowPlayingUiState is a stable data class).
    val nowPlaying: NowPlayingUiState? = homeState.currentMediaId?.let { id ->
        MockData.musicList.find { it.id == id }?.let { item ->
            NowPlayingUiState(
                trackId    = item.id,
                title      = item.title,
                subtitle   = item.subtitle,
                artworkUrl = item.imageUrl,
                progress   = homeState.playbackProgress,
                isPlaying  = homeState.isPlaying
            )
        }
    }

    // ── Wire to stateless UI ───────────────────────────────────────────────────
    HomeNavContent(
        state     = state,
        nowPlaying = nowPlaying,

        // ── Tab selection → HomeNavViewModel ──────────────────────────────────
        onTabSelected = { route ->
            viewModel.handleIntent(HomeNavContract.Intent.OnTabSelected(route))
        },

        // ── Player callbacks → HomeViewModel (explicit, named) ───────────────
        onTogglePlay = {
            homeViewModel.handleIntent(HomeContract.Intent.PlayPause)
        },
        onSkipNext = {
            homeViewModel.handleIntent(HomeContract.Intent.Next)
        },
        onSkipPrevious = {
            homeViewModel.handleIntent(HomeContract.Intent.Previous)
        },
        onFavorite = {
            // TODO: wire to a favourites repository when ready
        },
        onSeek = { fraction ->
            homeViewModel.handleIntent(HomeContract.Intent.Seek(fraction))
        },

        // ── Tab screen slots ──────────────────────────────────────────────────
        homeScreenContent = { contentPadding ->
            HomeScreen(
                contentPadding     = contentPadding,
                onNavigateToDetail = { id ->
                    viewModel.handleIntent(
                        HomeNavContract.Intent.OnDetailRequested(id, "home")
                    )
                }
            )
        },
        exploreScreenContent = { contentPadding ->
            ExploreScreen(
                contentPadding     = contentPadding,
                onNavigateToCategory = { id ->
                    viewModel.handleIntent(
                        HomeNavContract.Intent.OnDetailRequested(id, "explore")
                    )
                }
            )
        },
        libraryScreenContent = { contentPadding ->
            LibraryScreen(
                contentPadding = contentPadding,
                onSettingsClick = {
                    viewModel.handleIntent(HomeNavContract.Intent.OnSettingsClicked)
                }
            )
        }
    )
}
