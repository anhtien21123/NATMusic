package com.example.natmusic.presentation.feature_home

import com.example.natmusic.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeContract.Intent, HomeContract.State, HomeContract.Effect>() {
    override fun setInitialState() = HomeContract.State()

    override fun handleIntents(intent: HomeContract.Intent) {
        // Xử lý các intent từ UI cho màn hình Home
    }
}

