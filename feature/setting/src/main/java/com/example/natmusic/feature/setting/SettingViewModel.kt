package com.example.natmusic.feature.setting

import com.example.natmusic.core.mvi.BaseViewModel

/**
 * ViewModel for the Setting screen.
 *
 * Handles all [SettingContract.Intent]s and emits [SettingContract.SingleEvent]s
 * for one-time side effects (navigation, toasts, etc.).
 *
 * ── DFM candidate ────────────────────────────────────────────────────────────
 *  This ViewModel is loaded via [loadKoinModules(settingKoinModules)] on-demand
 *  inside [SettingScreen], supporting future Dynamic Feature Module delivery.
 */
class SettingViewModel : BaseViewModel<
    SettingContract.State,
    SettingContract.Intent,
    SettingContract.SingleEvent
>(
    initialState = SettingContract.State()
) {
    override fun handleIntent(intent: SettingContract.Intent) {
        when (intent) {
            // User tapped Back → emit a NavigateBack effect.
            // The UI collects it and calls navigator.navigateUp().
            SettingContract.Intent.OnBackClicked ->
                sendSingleEvent(SettingContract.SingleEvent.NavigateBack)
        }
    }
}
