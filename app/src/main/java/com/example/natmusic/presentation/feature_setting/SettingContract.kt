package com.example.natmusic.presentation.feature_setting

import com.example.natmusic.base.ViewIntent
import com.example.natmusic.base.ViewSideEffect
import com.example.natmusic.base.ViewState

class SettingContract {
    sealed class Intent : ViewIntent

    data class State(
        val isLoading: Boolean = false,
    ) : ViewState

    sealed class Effect : ViewSideEffect
}

