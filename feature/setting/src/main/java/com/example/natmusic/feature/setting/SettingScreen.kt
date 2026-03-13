package com.example.natmusic.feature.setting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.natmusic.core.common_ui.collectSingleEvent
import com.example.natmusic.feature.setting.di.settingKoinModules
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

/**
 * Stateful Container (Controller/Bridge) for Setting feature.
 * Coordinates between Dynamic Module loading, ViewModel, and the stateless UI.
 */
@Composable
fun SettingScreen(
    onBackClick: () -> Unit
) {
    // ── DFM: load modules synchronously before koinViewModel() ───────────────
    remember { loadKoinModules(settingKoinModules) }
    DisposableEffect(Unit) {
        onDispose { unloadKoinModules(settingKoinModules) }
    }

    val viewModel: SettingViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    // ── Side-effects: Observe SingleEvents (replaces LaunchedEffect) ─────────
    viewModel.singleEvent.collectSingleEvent { event ->
        when (event) {
            SettingContract.SingleEvent.NavigateBack -> onBackClick()
        }
    }

    // ── Glue everything to the stateless UI ──────────────────────────────────
    SettingContent(
        state = state,
        onBackClick = {
            // Mapping UI interaction directly to ViewModel intents or side effects
            onBackClick()
        }
    )
}
