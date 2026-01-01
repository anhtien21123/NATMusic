package com.example.natmusic.presentation.login

import com.example.natmusic.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : BaseViewModel<LoginContract.Intent, LoginContract.State, LoginContract.Effect>() {
    override fun setInitialState() = LoginContract.State()

    override fun handleIntents(intent: LoginContract.Intent) {
        // Xử lý các intent từ UI
    }
}
