package com.example.natmusic.feature.home.home

import com.example.natmusic.core.mvi.BaseViewModel

/**
 * Home-feed ViewModel.
 *
 * Extends [BaseViewModel] with typed [ViewState], [ViewIntent], [ViewSingleEvent].
 * Koin registers it via `viewModelOf(::HomeViewModel)` — zero annotations required.
 *
 * Adding a repository dependency is automatic:
 *   class HomeViewModel(private val repo: MusicRepository) : BaseViewModel<...>
 *   → Koin resolves MusicRepository via the module that binds it.
 */
class HomeViewModel : BaseViewModel<
    HomeContract.State,
    HomeContract.Intent,
    HomeContract.SingleEvent
>(
    initialState = HomeContract.State()
) {
    init { loadMockData() }

    override fun handleIntent(intent: HomeContract.Intent) {
        when (intent) {
            is HomeContract.Intent.OpenMusicItem ->
                sendSingleEvent(HomeContract.SingleEvent.NavigateToDetail(intent.id))
        }
    }

    private fun loadMockData() {
        val items = listOf(
            MusicItem("1", "Midnight Rain",  "Taylor Swift", "https://picsum.photos/seed/1/300/300"),
            MusicItem("2", "As It Was",      "Harry Styles", "https://picsum.photos/seed/2/300/300"),
            MusicItem("3", "Anti-Hero",      "Taylor Swift", "https://picsum.photos/seed/3/300/300"),
            MusicItem("4", "Flowers",        "Miley Cyrus",  "https://picsum.photos/seed/4/300/300"),
            MusicItem("5", "Kill Bill",      "SZA",          "https://picsum.photos/seed/5/300/300")
        )
        updateState {
            copy(
                recentItems = items.shuffled(),
                recommendedItems = items.shuffled(),
                trendingItems = items.shuffled()
            )
        }
    }
}

