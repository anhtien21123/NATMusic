package com.example.natmusic.feature.setting.domain.usecase

import com.example.natmusic.feature.setting.domain.model.UserProfile
import com.example.natmusic.feature.setting.domain.repository.SettingsRepository

class GetUserProfileUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): UserProfile = settingsRepository.getUserProfile()
}

class LogoutUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke() = settingsRepository.logout()
}
