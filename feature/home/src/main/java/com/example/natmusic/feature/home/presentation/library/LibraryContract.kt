package com.example.natmusic.feature.home.library

import com.example.natmusic.core.mvi.ViewIntent
import com.example.natmusic.core.mvi.ViewSingleEvent
import com.example.natmusic.core.mvi.ViewState

data class MediaItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val type: MediaType
)

enum class MediaType { PLAYLIST, ARTIST, ALBUM }

object LibraryContract {

    data class State(
        val isLoading: Boolean = false,
        val items: List<MediaItem> = emptyList(),
        val selectedFilter: MediaType? = null
    ) : ViewState

    sealed class Intent : ViewIntent {
        data class OpenItem(val id: String) : Intent()
        data class FilterByType(val type: MediaType?) : Intent()
        data object OnSettingsClick : Intent()
    }

    sealed class SingleEvent : ViewSingleEvent {
        data class NavigateToDetail(val id: String) : SingleEvent()
        data object NavigateToSettings : SingleEvent()
    }
}

