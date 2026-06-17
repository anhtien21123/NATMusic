package com.example.natmusic.feature.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
 * Stateful shell for the authenticated main graph (Home / Explore / Library).
 *
 * ── Navigation via LocalNavigator ────────────────────────────────────────────
 *
 *  Navigation callbacks are now completely decentralized and handled internally via
 *  [LocalNavigator] in their respective feature screens ([HomeScreen], [ExploreScreen], [LibraryScreen]).
 *  No callbacks are threaded back through AppNavHost.
 *
 * ── Two ViewModels, one shell ─────────────────────────────────────────────────
 *
 *  • [HomeNavViewModel] — owns tab-selection state ([HomeNavContract.State]).
 *  • [HomeViewModel]    — owns feed catalogues AND real-time playback state.
 *
 *  Both resolved via `koinViewModel()`. Koin scopes ViewModels to the nearest
 *  [ViewModelStoreOwner] (the Activity), so the same [HomeViewModel] instance
 *  is shared between this screen and [HomeScreen] — state changes propagate
 *  instantly without duplication.
 *
 * ── MiniPlayer wiring ─────────────────────────────────────────────────────────
 *
 *  [homeState.currentMediaId] is resolved against [MockData.musicList] to build
 *  a [NowPlayingUiState] snapshot (title, artwork, progress, isPlaying) that is
 *  passed down to [HomeNavContent] → [MiniPlayer] / [FullPlayerScreen].
 */
import androidx.compose.runtime.remember
import com.example.natmusic.core.common_ui.LocalNavigator
import com.example.natmusic.core.navigation.HomeNavigationContract
import com.example.natmusic.core.navigation.SettingNavigationContract

@Composable
fun HomeNavScreen(
    viewModel     : HomeNavViewModel = koinViewModel(),
    homeViewModel : HomeViewModel    = koinViewModel()
) {
    val settingContract = remember { org.koin.core.context.GlobalContext.get().get<SettingNavigationContract>() }
    val homeContract = remember { org.koin.core.context.GlobalContext.get().get<HomeNavigationContract>() }
    val navigator = LocalNavigator.current

    val state     by viewModel.state.collectAsStateWithLifecycle()
    val homeState by homeViewModel.state.collectAsStateWithLifecycle()

    // ── Derive MiniPlayer state from HomeViewModel ─────────────────────────────
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
        state      = state,
        nowPlaying = nowPlaying,

        // ── Tab selection → HomeNavViewModel ──────────────────────────────────
        onTabSelected = { route ->
            viewModel.handleIntent(HomeNavContract.Intent.OnTabSelected(route))
        },

        // ── Player callbacks → HomeViewModel (explicit, named) ───────────────
        onTogglePlay   = { homeViewModel.handleIntent(HomeContract.Intent.PlayPause) },
        onSkipNext     = { homeViewModel.handleIntent(HomeContract.Intent.Next) },
        onSkipPrevious = { homeViewModel.handleIntent(HomeContract.Intent.Previous) },
        onFavorite     = { /* TODO: wire to a favourites repository when ready */ },
        onSeek         = { fraction -> homeViewModel.handleIntent(HomeContract.Intent.Seek(fraction)) },

        // ── Tab screen slots ──────────────────────────────────────────────────
        homeScreenContent = { contentPadding ->
            HomeScreen(
                contentPadding = contentPadding,
                onNavigateToDetail = { id -> navigator.navigateTo(homeContract.detail(id, "home")) }
            )
        },
        exploreScreenContent = { contentPadding ->
            ExploreScreen(
                contentPadding = contentPadding,
                onNavigateToDetail = { id -> navigator.navigateTo(homeContract.detail(id, "explore")) }
            )
        },
        libraryScreenContent = { contentPadding ->
            LibraryScreen(
                contentPadding = contentPadding,
                onNavigateToSettings = { navigator.navigateTo(settingContract.setting) },
                onNavigateToDetail = { id -> navigator.navigateTo(homeContract.detail(id, "library")) }
            )
        }
    )
}
