package com.example.natmusic.presentation.login

import com.example.natmusic.base.ViewIntent
import com.example.natmusic.base.ViewSideEffect
import com.example.natmusic.base.ViewState

class LoginContract {
    sealed class Intent : ViewIntent

    data class State(
        val isLoading: Boolean = false,
    ) : ViewState

    sealed class Effect : ViewSideEffect
}
