package com.example.natmusic.feature.home.di


import com.example.natmusic.feature.home.HomeNavViewModel
import com.example.natmusic.feature.home.detail.DetailViewModel
import com.example.natmusic.feature.home.explore.ExploreViewModel
import com.example.natmusic.feature.home.home.HomeViewModel
import com.example.natmusic.feature.home.library.LibraryViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module definitions for :feature:home (always-present base module).
 *
 * [HomeViewModel] is registered once and shared by both [HomeScreen] and
 * [HomeNavScreen] via `koinViewModel()`. Because both composables live in
 * the same [ViewModelStoreOwner] (the Activity), Koin returns the same
 * instance — keeping playback state automatically in sync between the
 * MiniPlayer shell and the feed tab.
 */
val homeModule: Module = module {
    viewModelOf(::HomeNavViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::ExploreViewModel)
    viewModelOf(::LibraryViewModel)

    // DetailViewModel requires explicit parameters (id, origin) from NavEntry.
    factory { (id: String, origin: String) ->
        DetailViewModel(id = id, origin = origin)
    }
}

/** Typed list for loadKoinModules() / unloadKoinModules() if this module ever
 *  transitions to a real DFM split. */
val homeKoinModules: List<Module> = listOf(homeModule)
