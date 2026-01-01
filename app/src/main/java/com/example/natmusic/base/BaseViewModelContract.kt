package com.example.natmusic.base

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ViewIntent
interface ViewState
interface ViewSideEffect

interface BaseViewModelContract<INTENT : ViewIntent, STATE : ViewState, EFFECT : ViewSideEffect> {
    val uiState: StateFlow<STATE>
    val effect: SharedFlow<EFFECT>
    fun processIntent(intent: INTENT)
    fun setInitialState(): STATE
    fun handleIntents(intent: INTENT)
}
