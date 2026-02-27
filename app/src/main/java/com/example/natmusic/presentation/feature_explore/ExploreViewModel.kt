package com.example.natmusic.presentation.feature_explore

import com.example.natmusic.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor() : BaseViewModel<ExploreContract.Intent, ExploreContract.State, ExploreContract.Effect>() {
    override fun setInitialState() = ExploreContract.State()

    override fun handleIntents(intent: ExploreContract.Intent) {
        // Xử lý các intent từ UI cho màn hình Explore
    }
}

