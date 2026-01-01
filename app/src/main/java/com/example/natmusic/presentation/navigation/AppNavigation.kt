package com.example.natmusic.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.natmusic.R
import com.example.natmusic.presentation.feature_explore.ExploreScreen
import com.example.natmusic.presentation.feature_home.HomeScreen
import com.example.natmusic.presentation.feature_library.LibraryScreen
import com.example.natmusic.presentation.feature_setting.SettingScreen
import com.example.natmusic.presentation.login.LoginScreen
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object Login : Route
    @Serializable
    data object Home : Route
    @Serializable
    data object Explore : Route
    @Serializable
    data object Library : Route
    @Serializable
    data object Setting : Route
}

sealed class BottomNavItem(val route: Route, val title: String, val icon: Int) {
    data object Home : BottomNavItem(Route.Home, "Home", R.drawable.ic_home)
    data object Explore : BottomNavItem(Route.Explore, "Explore", R.drawable.ic_explore)
    data object Library :
        BottomNavItem(Route.Library, "Library", R.drawable.ic_library)
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Explore,
        BottomNavItem.Library
    )

    // Show BottomBar only on Home, Explore, and Library screens
    val showBottomBar = currentDestination?.hasRoute<Route.Home>() == true ||
            currentDestination?.hasRoute<Route.Explore>() == true ||
            currentDestination?.hasRoute<Route.Library>() == true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    androidx.compose.ui.res.painterResource(item.icon),
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(item.title) },
                            selected = currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Login,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Route.Login> {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Route.Home) {
                            popUpTo(Route.Login) { inclusive = true }
                        }
                    }
                )
            }
            composable<Route.Home> { HomeScreen() }
            composable<Route.Explore> { ExploreScreen() }
            composable<Route.Library> {
                LibraryScreen(
                    onSettingsClick = {
                        navController.navigate(Route.Setting)
                    }
                )
            }
            composable<Route.Setting> {
                SettingScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
