package com.example.natmusic.feature.setting.navigation

import com.example.natmusic.core.navigation.SettingDestination
import com.example.natmusic.core.navigation.SettingNavigationContract

/**
 * Concrete implementation of the shared [SettingDestination] for the setting module.
 */
data object Setting : SettingDestination

/**
 * Implementation of [SettingNavigationContract] that allows other modules to navigate
 * to destinations inside the setting feature.
 */
object SettingNavigationContractImpl : SettingNavigationContract {
    override val setting: SettingDestination = Setting
}
