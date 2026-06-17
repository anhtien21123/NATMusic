package com.example.natmusic.feature.setting.di

import com.example.natmusic.feature.setting.SettingViewModel
import com.example.natmusic.feature.setting.data.di.settingDataModule
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingPresentationModule: Module = module {
    viewModelOf(::SettingViewModel)
}

val settingKoinModules: List<Module> = listOf(settingDataModule, settingPresentationModule)
