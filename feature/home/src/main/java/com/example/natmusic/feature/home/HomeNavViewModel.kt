package com.example.natmusic.feature.home

import com.example.natmusic.core.mvi.BaseViewModel
import com.example.natmusic.core.navigation.MainRoute

class HomeNavViewModel : BaseViewModel<
    HomeNavContract.State,
    HomeNavContract.Intent,
    HomeNavContract.SingleEvent
>(
    initialState = HomeNavContract.State()
) {
    override fun handleIntent(intent: HomeNavContract.Intent) {
        when (intent) {
            is HomeNavContract.Intent.OnTabSelected ->
                updateState { copy(currentTab = intent.route) }

            HomeNavContract.Intent.OnSettingsClicked ->
                sendSingleEvent(HomeNavContract.SingleEvent.NavigateToSettings)

            is HomeNavContract.Intent.OnDetailRequested ->
                sendSingleEvent(
                    HomeNavContract.SingleEvent.NavigateToDetail(intent.id, intent.origin)
                )
        }
    }

    fun tabOrigin(route: MainRoute): String = when (route) {
        MainRoute.Home    -> "home"
        MainRoute.Explore -> "explore"
        MainRoute.Library -> "library"
    }
}
