package com.example.natmusic.feature.login.domain.usecase

import com.example.natmusic.feature.login.domain.model.AuthResult
import com.example.natmusic.feature.login.domain.repository.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        confirmPassword: String
    ): AuthResult = authRepository.register(email.trim(), password, confirmPassword)
}
