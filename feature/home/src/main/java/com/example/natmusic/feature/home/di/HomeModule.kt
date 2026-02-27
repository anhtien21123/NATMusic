package com.example.natmusic.feature.home.di

import com.example.natmusic.feature.home.HomeNavViewModel
import com.example.natmusic.feature.home.explore.ExploreViewModel
import com.example.natmusic.feature.home.home.HomeViewModel
import com.example.natmusic.feature.home.library.LibraryViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module definitions for :feature:home (always-present base module).
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * `viewModelOf(::ViewModel)` — Koin 4.x shorthand for constructor injection:
 *
 *   Old (manual):  viewModel { HomeViewModel(get(), get()) }
 *   New (DSL):     viewModelOf(::HomeViewModel)
 *
 * Koin auto-resolves all constructor parameters from the graph.
 * When a real use-case is added:
 *   class HomeViewModel(private val getMusicUseCase: GetMusicUseCase) : BaseViewModel<...>
 *   → viewModelOf(::HomeViewModel) resolves GetMusicUseCase automatically.
 *
 * Interface binding example for repositories:
 *   single { MusicRepositoryImpl(get()) } bind MusicRepository::class
 * ─────────────────────────────────────────────────────────────────────────────
 */
val homeModule: Module = module {
    viewModelOf(::HomeNavViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::ExploreViewModel)
    viewModelOf(::LibraryViewModel)
}

/** Typed list for loadKoinModules() / unloadKoinModules() if this module ever
 *  transitions to a real DFM split. */
val homeKoinModules: List<Module> = listOf(homeModule)

