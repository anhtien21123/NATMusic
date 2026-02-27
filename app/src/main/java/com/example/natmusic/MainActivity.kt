package com.example.natmusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.natmusic.core.common_ui.NATMusicTheme
import com.example.natmusic.navigation.AppNavHost

/**
 * Single Activity — the only Android entry point in the app.
 *
 * Responsibilities:
 *  1. Apply [NATMusicTheme] (from :core:common_ui — shared with all features).
 *  2. Host [AppNavHost] — the Nav3 top-level [NavDisplay].
 *  3. Forward the launch [Intent] (including deep links) via [LocalContext];
 *     [AppNavHost] reads it via DeepLinks.resolveBackStack(activity?.intent).
 *
 * No @AndroidEntryPoint, no hiltViewModel — Koin requires zero annotations.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NATMusicTheme {
                // AppNavHost is the sole owner of the NavBackStack<AppRoute>.
                // It creates / restores the back stack from the launch Intent,
                // handles deep links, and maps feature SingleEvents to nav mutations.
                AppNavHost()
            }
        }
    }
}
