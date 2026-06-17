package com.example.natmusic.feature.setting.domain.repository

import com.example.natmusic.feature.setting.domain.model.UserProfile

interface SettingsRepository {
    suspend fun getUserProfile(): UserProfile
    suspend fun logout()
}
