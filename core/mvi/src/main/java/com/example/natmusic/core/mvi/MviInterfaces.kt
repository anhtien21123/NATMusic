package com.example.natmusic.core.mvi

/**
 * Marker interface for all MVI ViewState types.
 *
 * Implement with an immutable data class:
 * ```
 * data class State(...) : ViewState
 * ```
 * [BaseViewModel] exposes state as [kotlinx.coroutines.flow.StateFlow] so the
 * last emitted value is always available to new collectors (survives
 * recomposition and screen rotation).
 */
interface ViewState

/**
 * Marker interface for all MVI ViewIntent types.
 *
 * Implement with a sealed class/interface representing every discrete
 * user action the screen can produce:
 * ```
 * sealed class Intent : ViewIntent {
 *     data class OnSearchQuery(val query: String) : Intent()
 *     data object OnRetryClicked : Intent()
 * }
 * ```
 * Intents are the ONLY public entry point into a ViewModel; the UI calls
 * `viewModel.handleIntent(Intent.OnRetryClicked)` and nothing else.
 */
interface ViewIntent

/**
 * Marker interface for one-shot UI side-effects that should be consumed
 * exactly once (navigation, snack-bars, dialogs, etc.).
 *
 * Implement with a sealed class/interface:
 * ```
 * sealed class SingleEvent : ViewSingleEvent {
 *     data class NavigateToDetail(val id: String) : SingleEvent()
 *     data class ShowError(val message: String)    : SingleEvent()
 * }
 * ```
 * [BaseViewModel] delivers these via a [kotlinx.coroutines.channels.Channel]
 * with `BUFFERED` capacity so events are never lost between emission and
 * the UI picking them up.
 *
 * Consume in Compose using the [collectSingleEvent] extension defined in
 * :core:common_ui.
 */
interface ViewSingleEvent

