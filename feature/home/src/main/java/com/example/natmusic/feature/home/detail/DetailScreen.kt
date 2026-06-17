package com.example.natmusic.feature.home.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.natmusic.core.common_ui.LocalNavigator
import com.example.natmusic.core.common_ui.collectSingleEvent
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Stateful container for the Detail screen.
 *
 * ── Navigation via LocalNavigator ────────────────────────────────────────────
 *
 *  Previously this screen received `onBack: () -> Unit` from AppNavHost.
 *  Navigation is now handled internally via [LocalNavigator]:
 *
 *   1. User taps Back                  → onBackClick lambda in DetailContent
 *   2. handleIntent(OnBackClicked)     → DetailViewModel
 *   3. sendSingleEvent(NavigateBack)   → effect channel
 *   4. collectSingleEvent              → navigator.navigateUp()
 *
 *  Route parameters ([id], [origin]) are passed directly as constructor
 *  arguments — no string encoding/decoding from the route URI is needed.
 *  Koin resolves [DetailViewModel] with these parameters via [parametersOf].
 *
 * @param id     Primary resource identifier — passed from [AppDestination.Detail.id].
 * @param origin Where navigation originated (analytics / back-stack rebuild).
 */
@Composable
fun DetailScreen(
    id      : String,
    origin  : String,
    viewModel: DetailViewModel = koinViewModel { parametersOf(id, origin) }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // ── Navigator (provided by AppNavHost via LocalNavigator) ─────────────────
    val navigator = LocalNavigator.current

    // ── One-time navigation effects ───────────────────────────────────────────
    viewModel.singleEvent.collectSingleEvent { event ->
        when (event) {
            DetailContract.SingleEvent.NavigateBack -> navigator.navigateUp()
        }
    }

    // ── Stateless UI ─────────────────────────────────────────────────────────
    DetailContent(
        state       = state,
        onBackClick = { viewModel.handleIntent(DetailContract.Intent.OnBackClicked) }
    )
}
