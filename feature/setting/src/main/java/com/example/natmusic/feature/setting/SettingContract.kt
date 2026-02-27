package com.example.natmusic.feature.setting

import com.example.natmusic.core.mvi.ViewIntent
import com.example.natmusic.core.mvi.ViewSingleEvent
import com.example.natmusic.core.mvi.ViewState

object SettingContract {

    data class State(
        val isLoading: Boolean = false
    ) : ViewState

    sealed class Intent : ViewIntent

    sealed class SingleEvent : ViewSingleEvent {
        data object NavigateBack : SingleEvent()
    }
}
