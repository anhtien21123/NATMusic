package com.example.natmusic.feature.setting.navigation

import androidx.compose.runtime.Composable
import com.example.natmusic.core.navigation.Navigator
import com.example.natmusic.core.navigation.SettingDestination
import com.example.natmusic.feature.setting.SettingScreen

/**
 * Setting feature content entry point. Renders setting screen when destination is [Setting].
 */
@Composable
fun SettingFeatureContent(
    destination: SettingDestination,
    navigator: Navigator
) {
    when (destination) {
        Setting -> SettingScreen()
    }
}
