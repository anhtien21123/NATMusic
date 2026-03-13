package com.example.natmusic.feature.home.explore

import androidx.compose.ui.graphics.Color
import com.example.natmusic.core.mvi.ViewIntent
import com.example.natmusic.core.mvi.ViewSingleEvent
import com.example.natmusic.core.mvi.ViewState

data class ExploreItem(
    val id: String,
    val title: String,
    val color: Color
)

object ExploreContract {

    data class State(
        val isLoading: Boolean = false,
        val categories: List<ExploreItem> = emptyList(),
        val searchQuery: String = ""
    ) : ViewState

    sealed class Intent : ViewIntent {
        data class OpenCategory(val id: String) : Intent()
        data class Search(val query: String) : Intent()
    }

    sealed class SingleEvent : ViewSingleEvent {
        data class NavigateToCategory(val id: String) : SingleEvent()
    }
}

