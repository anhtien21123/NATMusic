package com.example.natmusic.feature.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.natmusic.core.common_ui.collectSingleEvent
import com.example.natmusic.core.common_ui.LocalNavigator
import com.example.natmusic.core.navigation.AuthNavigationContract
import com.example.natmusic.core.navigation.HomeNavigationContract
import com.example.natmusic.feature.login.di.loginKoinModules
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

/**
 * Stateful container for the Login feature.
 *
 * ── Navigation via LocalNavigator ────────────────────────────────────────────
 *
 *  Previously this screen received `onLoginSuccess: () -> Unit` from AppNavHost,
 *  which created an upward callback chain. It now retrieves [Navigator] from
 *  [LocalNavigator] and handles all navigation directly:
 *
 *   1. User taps "Login" → UI calls viewModel.handleIntent(Submit)
 *   2. LoginViewModel validates → sendSingleEvent(NavigateToMain)
 *   3. collectSingleEvent collects the event
 *   4. navigator.navigateAndPopUp(Main, Login, inclusive = true)
 *      → Login is removed from the back-stack, Main becomes the root
 *
 *  No callbacks are threaded back through AppNavHost.
 *
 * ── DFM pattern ──────────────────────────────────────────────────────────────
 *
 *  Koin modules are loaded/unloaded dynamically to support future DFM delivery.
 *  Modules are loaded once (via [remember]) and unloaded on [DisposableEffect]
 *  cleanup when the screen leaves the composition.
 */
@Composable
fun LoginScreen() {

    // ── DFM: load modules synchronously before koinViewModel() ───────────────
    remember { loadKoinModules(loginKoinModules) }
    DisposableEffect(Unit) {
        onDispose { unloadKoinModules(loginKoinModules) }
    }

    val viewModel: LoginViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    // ── Navigation contracts resolved via Koin ────────────────────────────────
    val authContract = remember { org.koin.core.context.GlobalContext.get().get<AuthNavigationContract>() }
    val homeContract = remember { org.koin.core.context.GlobalContext.get().get<HomeNavigationContract>() }

    // ── Navigator (provided by AppNavHost via LocalNavigator) ─────────────────
    val navigator = LocalNavigator.current

    // ── One-time navigation effect ────────────────────────────────────────────
    //
    // collectSingleEvent is lifecycle-aware (repeatOnLifecycle(STARTED)) so
    // navigation only fires when the screen is visible — no stale events.
    viewModel.singleEvent.collectSingleEvent { event ->
        when (event) {
            LoginContract.SingleEvent.NavigateToMain ->
                // Pop Login (inclusive) then push Main → Login can never be
                // reached by pressing Back from the authenticated section.
                navigator.navigateAndPopUp(
                    destination = homeContract.main,
                    popUpTo     = authContract.login,
                    inclusive   = true
                )
        }
    }

    // ── Stateless UI ─────────────────────────────────────────────────────────
    LoginContent(
        state                    = state,
        onEmailChange            = { viewModel.handleIntent(LoginContract.Intent.OnEmailChange(it)) },
        onPasswordChange         = { viewModel.handleIntent(LoginContract.Intent.OnPasswordChange(it)) },
        onConfirmPasswordChange  = { viewModel.handleIntent(LoginContract.Intent.OnConfirmPasswordChange(it)) },
        onToggleMode             = { viewModel.handleIntent(LoginContract.Intent.ToggleMode) },
        onSubmit                 = { viewModel.handleIntent(LoginContract.Intent.Submit) }
    )
}
