package com.example.natmusic.di

import com.example.natmusic.core.service.impl.di.mediaImplModule
import com.example.natmusic.feature.home.data.di.homeDataModule
import com.example.natmusic.feature.home.di.homeKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

import com.example.natmusic.core.navigation.AuthNavigationContract
import com.example.natmusic.core.navigation.HomeNavigationContract
import com.example.natmusic.core.navigation.SettingNavigationContract
import com.example.natmusic.feature.login.navigation.AuthNavigationContractImpl
import com.example.natmusic.feature.home.navigation.HomeNavigationContractImpl
import com.example.natmusic.feature.setting.navigation.SettingNavigationContractImpl

// ─────────────────────────────────────────────────────────────────────────────
// App-level infrastructure module
// ─────────────────────────────────────────────────────────────────────────────
val appCoreModule: Module = module {
    single<AuthNavigationContract> { AuthNavigationContractImpl }
    single<HomeNavigationContract> { HomeNavigationContractImpl }
    single<SettingNavigationContract> { SettingNavigationContractImpl }
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
    listOf(appCoreModule, mediaImplModule, homeDataModule) + homeKoinModules
