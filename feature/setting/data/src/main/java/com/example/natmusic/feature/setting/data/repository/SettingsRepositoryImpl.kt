package com.example.natmusic.feature.setting.data.repository

import com.example.natmusic.feature.setting.domain.model.UserProfile
import com.example.natmusic.feature.setting.domain.repository.SettingsRepository
import kotlinx.coroutines.delay

class SettingsRepositoryImpl : SettingsRepository {

    override suspend fun getUserProfile(): UserProfile {
        delay(200)
        return UserProfile(
            id = "user-1",
            displayName = "Guest User",
            email = "guest@natmusic.example.com"
        )
    }

    override suspend fun logout() {
        delay(200)
    }
}
