package com.example.natmusic.feature.setting.di

import com.example.natmusic.feature.setting.SettingViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * ════════════════════════════════════════════════════════════════════
 *  :feature:setting Koin module — DFM candidate
 * ════════════════════════════════════════════════════════════════════
 *
 * Loaded via loadKoinModules(settingKoinModules) when the Settings screen
 * is navigated to, and unloaded via unloadKoinModules when leaving.
 *
 * Interface binding example when preferences repository is added:
 *   single<SettingsRepository> { SettingsRepositoryImpl(get()) } bind SettingsRepository::class
 */
val settingModule: Module = module {
    viewModelOf(::SettingViewModel)
}

val settingKoinModules: List<Module> = listOf(settingModule)

