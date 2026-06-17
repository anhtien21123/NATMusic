package com.example.natmusic.feature.login.navigation

import androidx.compose.runtime.Composable
import com.example.natmusic.core.navigation.AuthDestination
import com.example.natmusic.core.navigation.Navigator
import com.example.natmusic.feature.login.LoginScreen

/**
 * Auth feature content entry point. Renders login screen when destination is [Login].
 */
@Composable
fun AuthFeatureContent(
    destination: AuthDestination,
    navigator: Navigator
) {
    when (destination) {
        Login -> LoginScreen()
    }
}
