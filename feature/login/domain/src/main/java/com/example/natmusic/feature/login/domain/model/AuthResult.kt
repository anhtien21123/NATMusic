package com.example.natmusic.feature.login.domain.model

sealed interface AuthResult {
    data class Success(val user: User) : AuthResult
    data object InvalidCredentials : AuthResult
    data object PasswordMismatch : AuthResult
    data object WeakPassword : AuthResult
}
