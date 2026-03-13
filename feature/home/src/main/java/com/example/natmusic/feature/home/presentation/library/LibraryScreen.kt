package com.example.natmusic.feature.home.library

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.natmusic.core.common_ui.collectSingleEvent
import org.koin.androidx.compose.koinViewModel

/**
 * Stateful Container (Controller/Bridge) for Library feature.
 * Connects the ViewModel to the stateless UI (LibraryContent) via the [LibraryActions] interface.
 *
 * @param contentPadding Shell-supplied padding to clear the persistent MiniPlayer
 *                       and bottom navigation bar.
 */
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    onSettingsClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // ── Side-effects: Handle navigation & notifications ──────────────────────
    viewModel.singleEvent.collectSingleEvent { event ->
        when (event) {
            is LibraryContract.SingleEvent.NavigateToSettings -> onSettingsClick()
            else -> Unit
        }
    }

    // ── Actions: Map UI interaction directly to ViewModel intents ───────────
    val actions = remember(viewModel, onSettingsClick) {
        object : LibraryActions {
            override fun onFilterClick(type: MediaType?) {
                viewModel.handleIntent(LibraryContract.Intent.FilterByType(type))
            }
            override fun onItemClick(id: String) {
                viewModel.handleIntent(LibraryContract.Intent.OpenItem(id))
            }
            override fun onSettingsClick() {
                onSettingsClick()
            }
        }
    }

    // ── Connection ──
    LibraryContent(
        state          = state,
        actions        = actions,
        contentPadding = contentPadding
    )
}
