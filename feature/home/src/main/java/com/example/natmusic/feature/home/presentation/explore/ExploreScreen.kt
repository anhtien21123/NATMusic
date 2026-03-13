package com.example.natmusic.feature.home.explore

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.natmusic.core.common_ui.collectSingleEvent
import org.koin.androidx.compose.koinViewModel

/**
 * Stateful Container for the Explore feature.
 * Connects the ViewModel to the stateless UI (ExploreContent).
 *
 * @param contentPadding Shell-supplied padding to clear the persistent MiniPlayer
 *                       and bottom navigation bar.
 */
@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel = koinViewModel(),
    contentPadding: PaddingValues = PaddingValues(),
    onNavigateToCategory: (String) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.singleEvent.collectSingleEvent { event ->
        when (event) {
            is ExploreContract.SingleEvent.NavigateToCategory -> onNavigateToCategory(event.id)
        }
    }

    ExploreContent(
        state          = state,
        contentPadding = contentPadding,
        onSearchQueryChange = { query ->
            viewModel.handleIntent(ExploreContract.Intent.Search(query))
        },
        onCategoryClick = { id ->
            viewModel.handleIntent(ExploreContract.Intent.OpenCategory(id))
        }
    )
}
