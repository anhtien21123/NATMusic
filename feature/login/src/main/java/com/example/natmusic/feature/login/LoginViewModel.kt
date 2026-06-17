package com.example.natmusic.feature.login

import androidx.lifecycle.viewModelScope
import com.example.natmusic.core.mvi.BaseViewModel
import com.example.natmusic.feature.login.domain.model.AuthResult
import com.example.natmusic.feature.login.domain.usecase.LoginUseCase
import com.example.natmusic.feature.login.domain.usecase.RegisterUseCase
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : BaseViewModel<
    LoginContract.State,
    LoginContract.Intent,
    LoginContract.SingleEvent
>(
    initialState = LoginContract.State()
) {
    override fun handleIntent(intent: LoginContract.Intent) {
        when (intent) {
            is LoginContract.Intent.OnEmailChange ->
                updateState { copy(email = intent.email, errorMessage = null) }

            is LoginContract.Intent.OnPasswordChange ->
                updateState { copy(password = intent.password, errorMessage = null) }

            is LoginContract.Intent.OnConfirmPasswordChange ->
                updateState { copy(confirmPassword = intent.password, errorMessage = null) }

            LoginContract.Intent.ToggleMode ->
                updateState {
                    copy(
                        isRegister = !isRegister,
                        email = "",
                        password = "",
                        confirmPassword = "",
                        errorMessage = null
                    )
                }

            LoginContract.Intent.Submit -> submit()
        }
    }

    private fun submit() {
        val current = state.value
        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            val result = if (current.isRegister) {
                registerUseCase(current.email, current.password, current.confirmPassword)
            } else {
                loginUseCase(current.email, current.password)
            }
            when (result) {
                is AuthResult.Success -> {
                    updateState { copy(isLoading = false) }
                    sendSingleEvent(LoginContract.SingleEvent.NavigateToMain)
                }
                AuthResult.InvalidCredentials ->
                    updateState {
                        copy(isLoading = false, errorMessage = "Invalid email or password")
                    }
                AuthResult.PasswordMismatch ->
                    updateState {
                        copy(isLoading = false, errorMessage = "Passwords do not match")
                    }
                AuthResult.WeakPassword ->
                    updateState {
                        copy(isLoading = false, errorMessage = "Password must be at least 6 characters")
                    }
            }
        }
    }
}
