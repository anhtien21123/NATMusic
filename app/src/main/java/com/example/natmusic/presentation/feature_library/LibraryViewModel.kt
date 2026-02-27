package com.example.natmusic.presentation.feature_library

import com.example.natmusic.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor() : BaseViewModel<LibraryContract.Intent, LibraryContract.State, LibraryContract.Effect>() {
    override fun setInitialState() = LibraryContract.State()

    override fun handleIntents(intent: LibraryContract.Intent) {
        // Xử lý các intent từ UI cho màn hình Library
    }
}

