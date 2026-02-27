package com.example.natmusic.feature.setting

import com.example.natmusic.core.mvi.BaseViewModel

/**
 * Setting ViewModel — DFM candidate.
 * Loaded via loadKoinModules(settingKoinModules) on entering the screen.
 */
class SettingViewModel : BaseViewModel<
    SettingContract.State,
    SettingContract.Intent,
    SettingContract.SingleEvent
>(
    initialState = SettingContract.State()
) {
    override fun handleIntent(intent: SettingContract.Intent) {
        // Handle setting intents as the feature grows
    }
}
