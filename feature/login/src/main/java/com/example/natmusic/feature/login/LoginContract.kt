package com.example.natmusic.feature.login

import com.example.natmusic.core.mvi.ViewIntent
import com.example.natmusic.core.mvi.ViewSingleEvent
import com.example.natmusic.core.mvi.ViewState

object LoginContract {

    data class State(
        val isLoading: Boolean = false,
        val email: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val isRegister: Boolean = false
    ) : ViewState

    sealed class Intent : ViewIntent {
        data class OnEmailChange(val email: String) : Intent()
        data class OnPasswordChange(val password: String) : Intent()
        data class OnConfirmPasswordChange(val password: String) : Intent()
        data object ToggleMode : Intent()
        data object Submit : Intent()
    }

    sealed class SingleEvent : ViewSingleEvent {
        /**
         * Emitted on successful login/register.
         * :app maps this to navController.navigate(AppRoute.Home).
         * :feature:login has zero knowledge of AppRoute — no circular dep.
         */
        data object NavigateToMain : SingleEvent()
    }
}
