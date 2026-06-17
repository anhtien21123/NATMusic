package com.example.natmusic.feature.login.domain.usecase

import com.example.natmusic.feature.login.domain.model.AuthResult
import com.example.natmusic.feature.login.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): AuthResult =
        authRepository.login(email.trim(), password)
}
