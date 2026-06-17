package com.example.natmusic.feature.login.data.di

import com.example.natmusic.feature.login.data.repository.AuthRepositoryImpl
import com.example.natmusic.feature.login.domain.repository.AuthRepository
import com.example.natmusic.feature.login.domain.usecase.LoginUseCase
import com.example.natmusic.feature.login.domain.usecase.RegisterUseCase
import org.koin.dsl.module

val loginDataModule = module {
    single<AuthRepository> { AuthRepositoryImpl() }
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
}
