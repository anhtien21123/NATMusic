package com.example.natmusic.feature.home.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.natmusic.core.common_ui.collectSingleEvent
import com.example.natmusic.core.common_ui.LocalNavigator
import com.example.natmusic.core.navigation.HomeNavigationContract
import org.koin.androidx.compose.koinViewModel

/**
 * Stateful container for the Home feed tab.
 *
 * ── What changed ──────────────────────────────────────────────────────────────
 *  • [contentPadding] is now received from the parent shell ([HomeNavScreen])
 *    so the shell can dynamically adjust bottom clearance for the MiniPlayer.
 *  • All playback intents (PlayPause, Next, Previous, Seek) have been removed.
 *    The MiniPlayer callbacks now flow through [HomeNavScreen] → [MainViewModel]
 *    and never touch this ViewModel.
 *  • [HomeContent] now accepts only [onMusicItemClick] — a clean, minimal API.
 *
 * ── ONLY ViewModel injection point ───────────────────────────────────────────
 *  Per architectural rules, [HomeViewModel] is injected here and nowhere else.
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    onNavigateToDetail: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // ── Side-effects ──────────────────────────────────────────────────────────
    viewModel.singleEvent.collectSingleEvent { event ->
        when (event) {
            is HomeContract.SingleEvent.NavigateToDetail -> {
                onNavigateToDetail(event.id)
            }
        }
    }

    // ── Wire to stateless UI ──────────────────────────────────────────────────
    HomeContent(
        state          = state,
        contentPadding = contentPadding,
        onMusicItemClick = { id ->
            viewModel.handleIntent(HomeContract.Intent.OpenMusicItem(id))
        }
    )
}
