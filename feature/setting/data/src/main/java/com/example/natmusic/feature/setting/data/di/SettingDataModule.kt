package com.example.natmusic.feature.setting.data.di

import com.example.natmusic.feature.setting.data.repository.SettingsRepositoryImpl
import com.example.natmusic.feature.setting.domain.repository.SettingsRepository
import com.example.natmusic.feature.setting.domain.usecase.GetUserProfileUseCase
import com.example.natmusic.feature.setting.domain.usecase.LogoutUseCase
import org.koin.dsl.module

val settingDataModule = module {
    single<SettingsRepository> { SettingsRepositoryImpl() }
    factory { GetUserProfileUseCase(get()) }
    factory { LogoutUseCase(get()) }
}
