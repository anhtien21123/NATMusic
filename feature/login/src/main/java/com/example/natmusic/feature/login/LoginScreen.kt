package com.example.natmusic.feature.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.natmusic.core.common_ui.collectSingleEvent
import com.example.natmusic.feature.login.di.loginKoinModules
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

/**
 * Stateful Container (Controller/Bridge) for Login feature screens.
 * Handles DFM dynamic Koin module loading/unloading.
 * Collects state from ViewModel and maps Content callbacks to ViewModel intents.
 */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {
    // ── DFM: load modules synchronously before koinViewModel() ───────────────
    remember { loadKoinModules(loginKoinModules) }
    DisposableEffect(Unit) {
        onDispose { unloadKoinModules(loginKoinModules) }
    }

    val viewModel: LoginViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    // ── Lifecycle-aware SingleEvent collection (SideEffect handling) ──────
    viewModel.singleEvent.collectSingleEvent { event ->
        when (event) {
            LoginContract.SingleEvent.NavigateToMain -> onLoginSuccess()
        }
    }

    // ── Bind UI to Logic ──────────────────────────────────────────────────────
    LoginContent(
        state = state,
        onEmailChange = { viewModel.handleIntent(LoginContract.Intent.OnEmailChange(it)) },
        onPasswordChange = { viewModel.handleIntent(LoginContract.Intent.OnPasswordChange(it)) },
        onConfirmPasswordChange = { viewModel.handleIntent(LoginContract.Intent.OnConfirmPasswordChange(it)) },
        onToggleMode = { viewModel.handleIntent(LoginContract.Intent.ToggleMode) },
        onSubmit = { viewModel.handleIntent(LoginContract.Intent.Submit) }
    )
}
