package com.example.natmusic.feature.home.explore

import androidx.compose.ui.graphics.Color
import com.example.natmusic.core.mvi.BaseViewModel

class ExploreViewModel : BaseViewModel<
    ExploreContract.State,
    ExploreContract.Intent,
    ExploreContract.SingleEvent
>(
    initialState = ExploreContract.State()
) {
    init { loadMockData() }

    override fun handleIntent(intent: ExploreContract.Intent) {
        when (intent) {
            is ExploreContract.Intent.OpenCategory ->
                sendSingleEvent(ExploreContract.SingleEvent.NavigateToCategory(intent.id))
            is ExploreContract.Intent.Search ->
                updateState { copy(searchQuery = intent.query) }
        }
    }

    private fun loadMockData() {
        updateState {
            copy(
                categories = listOf(
                    ExploreItem("1",  "Pop",        Color(0xFFEF5350)),
                    ExploreItem("2",  "Rock",       Color(0xFFAB47BC)),
                    ExploreItem("3",  "Hip-Hop",    Color(0xFF42A5F5)),
                    ExploreItem("4",  "Jazz",       Color(0xFF26A69A)),
                    ExploreItem("5",  "Electronic", Color(0xFF66BB6A)),
                    ExploreItem("6",  "R&B",        Color(0xFFFFA726)),
                    ExploreItem("7",  "Classic",    Color(0xFF8D6E63)),
                    ExploreItem("8",  "Country",    Color(0xFF78909C)),
                    ExploreItem("9",  "Folk",       Color(0xFF5C6BC0)),
                    ExploreItem("10", "Blues",      Color(0xFF26C6DA))
                )
            )
        }
    }
}

