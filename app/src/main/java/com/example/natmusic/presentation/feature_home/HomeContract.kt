package com.example.natmusic.presentation.feature_home

import com.example.natmusic.base.ViewIntent
import com.example.natmusic.base.ViewSideEffect
import com.example.natmusic.base.ViewState

class HomeContract {
    sealed class Intent : ViewIntent

    data class State(
        val isLoading: Boolean = false,
    ) : ViewState

    sealed class Effect : ViewSideEffect
}

