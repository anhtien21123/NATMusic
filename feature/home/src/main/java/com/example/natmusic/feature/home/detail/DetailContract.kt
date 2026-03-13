package com.example.natmusic.feature.home.detail

import com.example.natmusic.core.mvi.ViewIntent
import com.example.natmusic.core.mvi.ViewSingleEvent
import com.example.natmusic.core.mvi.ViewState

/**
 * Detail screen MVI contract.
 */
object DetailContract {

    data class State(
        val id: String = "",
        val origin: String = "",
        val title: String = "",
        val isLoading: Boolean = true
    ) : ViewState

    sealed class Intent : ViewIntent {
        data object OnBackClicked : Intent()
    }

    sealed class SingleEvent : ViewSingleEvent {
        data object NavigateBack : SingleEvent()
    }
}
