package com.example.natmusic.feature.login.di

import com.example.natmusic.feature.login.LoginViewModel
import com.example.natmusic.feature.login.data.di.loginDataModule
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val loginPresentationModule: Module = module {
    viewModelOf(::LoginViewModel)
}

val loginKoinModules: List<Module> = listOf(loginDataModule, loginPresentationModule)
