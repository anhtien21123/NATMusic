package com.example.natmusic.feature.setting

import com.example.natmusic.core.mvi.ViewIntent
import com.example.natmusic.core.mvi.ViewSingleEvent
import com.example.natmusic.core.mvi.ViewState

/**
 * MVI contract for the Setting screen.
 *
 * ── Intent flow (back navigation) ─────────────────────────────────────────────
 *
 *  Previously [SettingScreen] called `onBackClick()` directly without routing
 *  through the ViewModel — a violation of the UDF principle where ALL user
 *  actions must enter through [ViewIntent].
 *
 *  Corrected flow:
 *   UI taps Back  →  handleIntent(OnBackClicked)
 *                →  ViewModel emits NavigateBack
 *                →  collectSingleEvent { navigator.navigateUp() }
 */
object SettingContract {

    data class State(
        val isLoading: Boolean = false
    ) : ViewState

    sealed class Intent : ViewIntent {
        /** User tapped the back / up arrow. */
        data object OnBackClicked : Intent()
    }

    sealed class SingleEvent : ViewSingleEvent {
        /**
         * Instructs the UI to pop this screen.
         * [SettingScreen] maps this to [Navigator.navigateUp].
         */
        data object NavigateBack : SingleEvent()
    }
}
