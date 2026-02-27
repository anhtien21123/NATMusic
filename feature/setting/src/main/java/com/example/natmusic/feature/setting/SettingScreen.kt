package com.example.natmusic.feature.setting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.natmusic.core.common_ui.collectSingleEvent
import org.koin.androidx.compose.koinViewModel
import com.example.natmusic.feature.setting.di.settingKoinModules
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

/**
 * Setting screen — DFM dynamic module loading pattern (identical to LoginScreen).
 *
 *  LOAD   → remember { loadKoinModules(settingKoinModules) }
 *  UNLOAD → DisposableEffect.onDispose { unloadKoinModules(settingKoinModules) }
 *
 * [onBackClick] is provided by :app's AppNavigation — no navController / AppRoute
 * imports inside this module.
 */
@OptIn(ExperimentalMaterial3Api::class)
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

    // Observe SingleEvents (e.g. back navigation triggered programmatically)
    viewModel.singleEvent.collectSingleEvent { event ->
        when (event) {
            SettingContract.SingleEvent.NavigateBack -> onBackClick()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Settings Screen")
        }
    }
}
