package com.example.natmusic.feature.home.library

import com.example.natmusic.core.mvi.ViewIntent
import com.example.natmusic.core.mvi.ViewSingleEvent
import com.example.natmusic.core.mvi.ViewState
import com.example.natmusic.feature.home.domain.model.LibraryItemType

object LibraryContract {

    data class State(
        val isLoading: Boolean = false,
        val items: List<LibraryItemUi> = emptyList(),
        val selectedFilter: LibraryItemType? = null
    ) : ViewState

    sealed class Intent : ViewIntent {
        data class OpenItem(val id: String) : Intent()
        data class FilterByType(val type: LibraryItemType?) : Intent()
        data object OnSettingsClick : Intent()
    }

    sealed class SingleEvent : ViewSingleEvent {
        data class NavigateToDetail(val id: String) : SingleEvent()
        data object NavigateToSettings : SingleEvent()
    }
}

