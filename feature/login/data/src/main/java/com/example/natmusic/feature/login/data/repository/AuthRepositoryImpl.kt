package com.example.natmusic.feature.login.data.repository

import com.example.natmusic.feature.login.domain.model.AuthResult
import com.example.natmusic.feature.login.domain.model.User
import com.example.natmusic.feature.login.domain.repository.AuthRepository
import kotlinx.coroutines.delay

class AuthRepositoryImpl : AuthRepository {

    private var currentUser: User? = null

    override suspend fun login(email: String, password: String): AuthResult {
        delay(400)
        if (email.isBlank() || password.length < 6) {
            return AuthResult.InvalidCredentials
        }
        val user = User(id = "user-1", email = email)
        currentUser = user
        return AuthResult.Success(user)
    }

    override suspend fun register(
        email: String,
        password: String,
        confirmPassword: String
    ): AuthResult {
        delay(400)
        if (password.length < 6) return AuthResult.WeakPassword
        if (password != confirmPassword) return AuthResult.PasswordMismatch
        if (email.isBlank()) return AuthResult.InvalidCredentials
        val user = User(id = "user-1", email = email)
        currentUser = user
        return AuthResult.Success(user)
    }

    override fun isAuthenticated(): Boolean = currentUser != null
}
