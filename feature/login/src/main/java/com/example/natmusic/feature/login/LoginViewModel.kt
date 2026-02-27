package com.example.natmusic.feature.login

import com.example.natmusic.core.mvi.BaseViewModel

/**
 * Login ViewModel — DFM candidate.
 *
 * Extends [BaseViewModel] with [ViewState], [ViewIntent], [ViewSingleEvent]
 * marker interfaces. Registered in [loginModule] via `viewModelOf(::LoginViewModel)`.
 *
 * DFM pattern:
 *  • This ViewModel only exists while [loginKoinModules] is loaded.
 *  • When unloadKoinModules() is called, Koin removes the factory from
 *    the graph; the next ViewModelStore.clear() frees memory entirely.
 *
 * Future: inject AuthRepository as a constructor parameter:
 *   class LoginViewModel(private val authRepo: AuthRepository) : BaseViewModel<...>
 *   → viewModelOf(::LoginViewModel) resolves AuthRepository automatically.
 */
class LoginViewModel : BaseViewModel<
    LoginContract.State,
    LoginContract.Intent,
    LoginContract.SingleEvent
>(
    initialState = LoginContract.State()
) {
    override fun handleIntent(intent: LoginContract.Intent) {
        when (intent) {
            is LoginContract.Intent.OnEmailChange ->
                updateState { copy(email = intent.email) }

            is LoginContract.Intent.OnPasswordChange ->
                updateState { copy(password = intent.password) }

            is LoginContract.Intent.OnConfirmPasswordChange ->
                updateState { copy(confirmPassword = intent.password) }

            LoginContract.Intent.ToggleMode ->
                updateState {
                    copy(isRegister = !isRegister, email = "", password = "", confirmPassword = "")
                }

            LoginContract.Intent.Submit -> {
                // TODO: call AuthRepository; mock success for now
                updateState { copy(isLoading = true) }
                sendSingleEvent(LoginContract.SingleEvent.NavigateToMain)
            }
        }
    }
}
