package com.example.natmusic.feature.login.di

import com.example.natmusic.feature.login.LoginViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * ════════════════════════════════════════════════════════════════════
 *  :feature:login Koin module — DFM candidate
 * ════════════════════════════════════════════════════════════════════
 *
 * This module is intentionally NOT registered in startKoin{} in the
 * Application class. Instead it is loaded/unloaded dynamically:
 *
 *   LOAD   (when navigating to Login or after DFM split installs):
 *     loadKoinModules(loginKoinModules)
 *
 *   UNLOAD (when leaving Login or DFM split uninstalls):
 *     unloadKoinModules(loginKoinModules)
 *
 * See LoginScreen.kt for the Compose-lifecycle-aware loading pattern.
 *
 * Interface binding example (when AuthRepository is ready):
 *   val loginDataModule = module {
 *       single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
 *   }
 */
val loginModule: Module = module {
    viewModelOf(::LoginViewModel)
}

/** Expose as a typed list for loadKoinModules() / unloadKoinModules(). */
val loginKoinModules: List<Module> = listOf(loginModule)

