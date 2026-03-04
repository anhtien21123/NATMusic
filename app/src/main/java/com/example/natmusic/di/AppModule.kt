package com.example.natmusic.di

import com.example.natmusic.core.service.di.serviceModule
import com.example.natmusic.feature.home.di.homeKoinModules
import com.example.natmusic.ui.DetailViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

// ─────────────────────────────────────────────────────────────────────────────
// Detail ViewModel — lives in :app because DetailScreen is an :app-level screen
// (move to :feature:detail when that module is created)
// ─────────────────────────────────────────────────────────────────────────────
private val detailModule: Module = module {
    /**
     * factory (not single!) because a new DetailViewModel should be created
     * for every navigation to Detail, with fresh constructor parameters.
     *
     * Koin resolves (id, origin) from the parametersOf() call in DetailScreen:
     *   koinViewModel { parametersOf(id, origin) }
     *
     * Nav3 + lifecycle-viewmodel-navigation3 clears this ViewModel's store
     * automatically when the NavEntry is popped from the back stack.
     */
    factory { (id: String, origin: String) -> DetailViewModel(id, origin) }
}

// ─────────────────────────────────────────────────────────────────────────────
// App-level infrastructure module
// ─────────────────────────────────────────────────────────────────────────────
val appCoreModule: Module = module {
    // TODO: single { AnalyticsTracker(androidContext()) }
    // TODO: single<SessionManager> { SessionManagerImpl(get()) }
}

// ─────────────────────────────────────────────────────────────────────────────
// Startup modules (loaded once in NATMusicApplication.startKoin)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * ┌──────────────────────────────────────────────────────────────────────────┐
 * │  Modules loaded at APPLICATION START                                     │
 * │                                                                          │
 * │  appCoreModule   – :app infrastructure (analytics, session, etc.)        │
 * │  detailModule    – DetailViewModel (factory, scoped per Nav3 entry)      │
 * │  homeKoinModules – HomeNavViewModel + Home/Explore/Library ViewModels    │
 * │                                                                          │
 * │  DFM modules (:feature:login, :feature:setting) are NOT included here.  │
 * │  They self-register via:                                                 │
 * │    remember { loadKoinModules(loginKoinModules) }   in LoginScreen       │
 * │    remember { loadKoinModules(settingKoinModules) } in SettingScreen     │
 * └──────────────────────────────────────────────────────────────────────────┘
 */
val startupModules: List<Module> =
    listOf(appCoreModule, detailModule, serviceModule) + homeKoinModules
