package com.example.natmusic.feature.home.explore

import com.example.natmusic.core.mvi.ViewIntent
import com.example.natmusic.core.mvi.ViewSingleEvent
import com.example.natmusic.core.mvi.ViewState

object ExploreContract {

    data class State(
        val isLoading: Boolean = false,
        val categories: List<ExploreItemUi> = emptyList(),
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

