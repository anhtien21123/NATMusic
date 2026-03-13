package com.example.natmusic.di

import com.example.natmusic.core.service.impl.di.mediaImplModule
import com.example.natmusic.feature.home.di.homeKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

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
    listOf(appCoreModule, mediaImplModule) + homeKoinModules
