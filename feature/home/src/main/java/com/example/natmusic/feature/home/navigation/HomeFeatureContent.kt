package com.example.natmusic.feature.home.navigation

import androidx.compose.runtime.Composable
import com.example.natmusic.core.navigation.HomeDestination
import com.example.natmusic.core.navigation.Navigator
import com.example.natmusic.feature.home.detail.DetailScreen
import com.example.natmusic.feature.home.presentation.HomeNavScreen

/**
 * Home feature content entry point. Renders appropriate screen based on [HomeDestination].
 */
@Composable
fun HomeFeatureContent(
    destination: HomeDestination,
    navigator: Navigator
) {
    when (destination) {
        Main -> HomeNavScreen()
        is Detail -> DetailScreen(
            id = destination.id,
            origin = destination.origin
        )
    }
}
