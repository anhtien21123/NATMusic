package com.example.natmusic.feature.home.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.natmusic.core.common_ui.collectSingleEvent
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Stateful Container (Controller/Bridge) for the Detail screen.
 * Coordinates between the typed route parameters, ViewModel, and the stateless UI.
 */
@Composable
fun DetailScreen(
    id: String,
    origin: String,
    onBack: () -> Unit,
    // Koin injects DetailViewModel and auto-resolves id + origin from parameters
    viewModel: DetailViewModel = koinViewModel { parametersOf(id, origin) }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // ── SideEffects (Navigation back) ───────────────────────
    viewModel.singleEvent.collectSingleEvent { event ->
        when (event) {
            DetailContract.SingleEvent.NavigateBack -> onBack()
        }
    }

    // ── Connect UI to Intents ───────────────────────────────
    DetailContent(
        state = state,
        onBackClick = {
            viewModel.handleIntent(DetailContract.Intent.OnBackClicked)
        }
    )
}
