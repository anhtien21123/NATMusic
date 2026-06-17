package com.example.natmusic.feature.setting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.natmusic.core.common_ui.collectSingleEvent
import com.example.natmusic.core.common_ui.LocalNavigator
import com.example.natmusic.feature.setting.di.settingKoinModules
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

/**
 * Stateful container for the Setting screen.
 *
 * ── Navigation via LocalNavigator ────────────────────────────────────────────
 *
 *  Previously this screen received `onBackClick: () -> Unit` from AppNavHost,
 *  and called it directly without routing through the ViewModel — bypassing
 *  the UDF principle.
 *
 *  Corrected MVI back-navigation flow:
 *   1. User taps Back                 → onBackClick lambda
 *   2. handleIntent(OnBackClicked)    → ViewModel
 *   3. sendSingleEvent(NavigateBack)  → effect channel
 *   4. collectSingleEvent             → navigator.navigateUp()
 *
 *  No callbacks are threaded back through AppNavHost.
 *
 * ── DFM pattern ──────────────────────────────────────────────────────────────
 *
 *  Koin modules are loaded/unloaded dynamically; no static module registration
 *  in Application is required for this feature.
 */
@Composable
fun SettingScreen() {

    // ── DFM: load modules synchronously before koinViewModel() ───────────────
    remember { loadKoinModules(settingKoinModules) }
    DisposableEffect(Unit) {
        onDispose { unloadKoinModules(settingKoinModules) }
    }

    val viewModel: SettingViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    // ── Navigator (provided by AppNavHost via LocalNavigator) ─────────────────
    val navigator = LocalNavigator.current

    // ── One-time navigation effects ───────────────────────────────────────────
    viewModel.singleEvent.collectSingleEvent { event ->
        when (event) {
            SettingContract.SingleEvent.NavigateBack -> navigator.navigateUp()
        }
    }

    // ── Stateless UI ─────────────────────────────────────────────────────────
    SettingContent(
        state       = state,
        onBackClick = { viewModel.handleIntent(SettingContract.Intent.OnBackClicked) }
    )
}
