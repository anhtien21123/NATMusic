package com.example.natmusic.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<INTENT : ViewIntent, STATE : ViewState, EFFECT : ViewSideEffect> :
    ViewModel(), BaseViewModelContract<INTENT, STATE, EFFECT> {

    private val initialState: STATE by lazy { setInitialState() }

    private val _uiState: MutableStateFlow<STATE> = MutableStateFlow(initialState)
    override val uiState = _uiState.asStateFlow()

    private val _intent: MutableSharedFlow<INTENT> = MutableSharedFlow()

    private val _effect: Channel<EFFECT> = Channel()
    override val effect = _effect.receiveAsFlow()

    init {
        subscribeIntents()
    }

    private fun subscribeIntents() {
        viewModelScope.launch {
            _intent.collect {
                handleIntents(it)
            }
        }
    }

    override fun processIntent(intent: INTENT) {
        viewModelScope.launch { _intent.emit(intent) }
    }

    protected fun setState(reducer: STATE.() -> STATE) {
        val newState = uiState.value.reducer()
        _uiState.value = newState
    }

    protected fun setEffect(builder: () -> EFFECT) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }
}
