package com.example.natmusic.feature.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.natmusic.core.common_ui.collectSingleEvent
import com.example.natmusic.core.common_ui.component.NatButton
import com.example.natmusic.core.common_ui.component.NatTextAction
import com.example.natmusic.core.common_ui.component.NatTextField
import org.koin.androidx.compose.koinViewModel
import com.example.natmusic.feature.login.di.loginKoinModules
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

/**
 * Login / Register screen — DFM dynamic module loading pattern.
 *
 * ═══════════════════════════════════════════════════════════════════════
 *  Koin Dynamic Module Lifecycle
 * ═══════════════════════════════════════════════════════════════════════
 *
 *  LOAD  → [remember] calls [loadKoinModules] on first composition.
 *           This is **synchronous**: modules are in the Koin graph before
 *           [koinViewModel] is called on the next line. No race condition.
 *
 *  UNLOAD → [DisposableEffect.onDispose] calls [unloadKoinModules] when
 *            the composable leaves the composition (screen is popped).
 *            The factory is removed from Koin's graph; memory reclaimed.
 *
 *  In a real Play Feature Delivery app:
 *    1. Download split APK via SplitInstallManager.
 *    2. In SplitInstallStateUpdatedListener (INSTALLED):
 *         loadKoinModules(loginKoinModules)
 *    3. Navigate to the login destination.
 *    4. On uninstall / session end:
 *         unloadKoinModules(loginKoinModules)
 *
 * ═══════════════════════════════════════════════════════════════════════
 *
 * [onLoginSuccess] is a lambda from :app's AppNavigation — this screen
 * has NO import of AppRoute, keeping the module graph acyclic.
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

    // ── Lifecycle-aware SingleEvent collection (replaces LaunchedEffect) ──────
    viewModel.singleEvent.collectSingleEvent { event ->
        when (event) {
            LoginContract.SingleEvent.NavigateToMain -> onLoginSuccess()
        }
    }

    // ── UI ────────────────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = if (state.isRegister) "Create Account" else "Welcome Back",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            NatTextField(
                value = state.email,
                onValueChange = { viewModel.handleIntent(LoginContract.Intent.OnEmailChange(it)) },
                placeholder = "Email",
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            NatTextField(
                value = state.password,
                onValueChange = { viewModel.handleIntent(LoginContract.Intent.OnPasswordChange(it)) },
                placeholder = "Password",
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = if (state.isRegister) ImeAction.Next else ImeAction.Done
                )
            )

            if (state.isRegister) {
                NatTextField(
                    value = state.confirmPassword,
                    onValueChange = { viewModel.handleIntent(LoginContract.Intent.OnConfirmPasswordChange(it)) },
                    placeholder = "Confirm Password",
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            NatButton(
                text = if (state.isRegister) "Sign Up" else "Login",
                onClick = { viewModel.handleIntent(LoginContract.Intent.Submit) }
            )

            NatTextAction(
                text = if (state.isRegister) "Already have an account?" else "Don't have an account?",
                actionText = if (state.isRegister) "Login" else "Sign Up",
                onActionClick = { viewModel.handleIntent(LoginContract.Intent.ToggleMode) }
            )
        }
    }
}
