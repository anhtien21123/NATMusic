package com.example.natmusic

import android.app.Application
import com.example.natmusic.di.startupModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Application entry point for NATMusic.
 *
 * ── Koin initialization (replaces @HiltAndroidApp) ────────────────────────
 *  No annotation processing · No KSP · No KAPT
 *
 *  Modules loaded at startup ([startupModules]):
 *   • appCoreModule   – :app infrastructure
 *   • homeKoinModules – always-present bottom-nav features
 *
 *  DFM modules (:feature:login, :feature:setting) are EXCLUDED from startup.
 *  They use the load/unload pattern inside their entry composables:
 *
 *    // On enter (synchronous – before koinViewModel()):
 *    remember { loadKoinModules(loginKoinModules) }
 *
 *    // On exit (DisposableEffect):
 *    DisposableEffect(Unit) {
 *        onDispose { unloadKoinModules(loginKoinModules) }
 *    }
 *
 *  For real Play Feature Delivery:
 *    SplitInstallManager.startInstall(request)
 *    // In SplitInstallStateUpdatedListener when state == INSTALLED:
 *    loadKoinModules(loginKoinModules)
 *    navController.navigate(AppDestination.Login)
 * ──────────────────────────────────────────────────────────────────────────
 */
class NATMusicApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NATMusicApplication)
            // Switch to Level.ERROR for release builds
            androidLogger(Level.DEBUG)
            modules(startupModules)
        }
    }
}
