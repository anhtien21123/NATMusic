package com.example.natmusic.feature.login.domain.repository

import com.example.natmusic.feature.login.domain.model.AuthResult

interface AuthRepository {
    suspend fun login(email: String, password: String): AuthResult
    suspend fun register(email: String, password: String, confirmPassword: String): AuthResult
    fun isAuthenticated(): Boolean
}
