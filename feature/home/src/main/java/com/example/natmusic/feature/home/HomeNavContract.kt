package com.example.natmusic.feature.home

import com.example.natmusic.core.mvi.ViewIntent
import com.example.natmusic.core.mvi.ViewSingleEvent
import com.example.natmusic.core.mvi.ViewState
import com.example.natmusic.core.navigation.MainRoute

/**
 * MVI contract for the home-container (bottom-nav shell).
 *
 * [MainRoute] is imported from :core:navigation so that any future feature
 * module can reference the tab routes without depending on :feature:home.
 *
 * Navigation effects bubble up as [SingleEvent] to :app's AppNavHost, which
 * maps them to the correct [AppRoute] navigation without :feature:home ever
 * importing AppRoute or NavController.
 */
object HomeNavContract {

    data class State(
        val currentTab: MainRoute = MainRoute.Home
    ) : ViewState

    sealed class Intent : ViewIntent {
        data class OnTabSelected(val route: MainRoute) : Intent()
        data object OnSettingsClicked : Intent()
        data class OnDetailRequested(val id: String, val origin: String) : Intent()
    }

    sealed class SingleEvent : ViewSingleEvent {
        /** Propagated to :app → navController.navigate(AppRoute.Setting) */
        data object NavigateToSettings : SingleEvent()

        /**
         * Propagated to :app → navController.navigate(AppRoute.Detail(id, origin))
         * origin = tab name, e.g. "home", "explore", "library"
         */
        data class NavigateToDetail(val id: String, val origin: String) : SingleEvent()
    }
}
