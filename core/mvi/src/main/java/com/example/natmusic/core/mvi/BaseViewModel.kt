package com.example.natmusic.core.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Base MVI ViewModel for the NATMusic app.
 *
 * ────────────────────────────────────────────────────────────────────────────
 * Type parameters (bounded by marker interfaces from [MviInterfaces.kt]):
 *
 *  [S] : [ViewState]       – Immutable data class; what the UI renders.
 *  [I] : [ViewIntent]      – Sealed class of user actions; single entry point.
 *  [SE]: [ViewSingleEvent] – Sealed class for one-shot side-effects
 *                            (navigation, snack-bars, dialogs).
 * ────────────────────────────────────────────────────────────────────────────
 *
 * Design decisions:
 *  • [state] uses [StateFlow] → latest value is always replayed to new
 *    collectors (survives recomposition and screen rotation).
 *  • [singleEvent] uses [Channel.BUFFERED] → events queued when no collector
 *    is active, ensuring zero event loss across Compose lifecycle transitions.
 *  • [updateState] applies an immutable reducer lambda, matching Kotlin
 *    data-class `copy {}` idiom.
 *  • [sendSingleEvent] always dispatches on [viewModelScope] — callers
 *    never need to think about the coroutine context.
 *
 * ────────────────────────────────────────────────────────────────────────────
 * Usage example:
 * ```kotlin
 * class HomeViewModel : BaseViewModel<
 *     HomeContract.State,
 *     HomeContract.Intent,
 *     HomeContract.SingleEvent>(
 *     initialState = HomeContract.State()
 * ) {
 *     override fun handleIntent(intent: HomeContract.Intent) {
 *         when (intent) {
 *             is HomeContract.Intent.OpenItem ->
 *                 sendSingleEvent(HomeContract.SingleEvent.NavigateToDetail(intent.id))
 *         }
 *     }
 * }
 *
 * // In Composable:
 * val state by viewModel.state.collectAsStateWithLifecycle()
 * viewModel.singleEvent.collectSingleEvent { event ->
 *     when (event) {
 *         is HomeContract.SingleEvent.NavigateToDetail -> onNavigate(event.id)
 *     }
 * }
 * ```
 */
abstract class BaseViewModel<S : ViewState, I : ViewIntent, SE : ViewSingleEvent>(
    initialState: S
) : ViewModel() {

    // ── State ─────────────────────────────────────────────────────────────────
    private val _state = MutableStateFlow(initialState)

    /**
     * Read-only stream of UI state.
     * Collect with `collectAsStateWithLifecycle()` (from lifecycle-runtime-compose).
     */
    val state: StateFlow<S> = _state.asStateFlow()

    // ── Single Events ─────────────────────────────────────────────────────────
    private val _singleEvent = Channel<SE>(Channel.BUFFERED)

    /**
     * One-shot events: navigation, toasts, dialogs.
     * Collect with the `collectSingleEvent` extension from :core:common_ui,
     * which is lifecycle-aware via `repeatOnLifecycle(STARTED)`.
     */
    val singleEvent: Flow<SE> = _singleEvent.receiveAsFlow()

    // ── Public API ────────────────────────────────────────────────────────────

    /** The UI's single entry-point for all user actions. */
    abstract fun handleIntent(intent: I)

    // ── Protected helpers ─────────────────────────────────────────────────────

    /**
     * Atomically updates the state using an immutable reducer.
     *
     * Example: `updateState { copy(isLoading = true) }`
     */
    protected fun updateState(reducer: S.() -> S) {
        _state.update { it.reducer() }
    }

    /**
     * Emits a one-shot [ViewSingleEvent] to the UI.
     * Safe to call from any thread / coroutine context.
     */
    protected fun sendSingleEvent(event: SE) {
        viewModelScope.launch {
            _singleEvent.send(event)
        }
    }
}
