package com.example.natmusic.presentation.feature_setting

import com.example.natmusic.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor() : BaseViewModel<SettingContract.Intent, SettingContract.State, SettingContract.Effect>() {
    override fun setInitialState() = SettingContract.State()

    override fun handleIntents(intent: SettingContract.Intent) {
        // Xử lý các intent từ UI cho màn hình Setting
    }
}

