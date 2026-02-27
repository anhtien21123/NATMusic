package com.example.natmusic.core.common_ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.natmusic.core.mvi.ViewSingleEvent
import kotlinx.coroutines.flow.Flow

/**
 * Lifecycle-aware collector for [ViewSingleEvent] streams.
 *
 * ────────────────────────────────────────────────────────────────────────────
 * Why lifecycle-aware?
 *  Using a plain `LaunchedEffect { flow.collect { ... } }` can trigger
 *  navigation or show dialogs while the screen is in the back-stack or
 *  the Activity is paused/stopped, causing visual artefacts or ANRs.
 *  [repeatOnLifecycle] suspends collection below [minActiveState] and
 *  automatically resumes it when the lifecycle re-enters that state.
 *
 * Why [rememberUpdatedState] for [onEvent]?
 *  The lambda captured by [LaunchedEffect] is fixed on the first composition.
 *  Wrapping [onEvent] in [rememberUpdatedState] ensures the coroutine always
 *  invokes the *latest* version of the lambda without restarting the collector.
 *
 * ────────────────────────────────────────────────────────────────────────────
 * Usage (inside any @Composable):
 * ```kotlin
 * val viewModel: HomeViewModel = koinViewModel()
 *
 * viewModel.singleEvent.collectSingleEvent { event ->
 *     when (event) {
 *         is HomeContract.SingleEvent.NavigateToDetail -> onNavigate(event.id)
 *         is HomeContract.SingleEvent.ShowError        -> showSnackbar(event.msg)
 *     }
 * }
 * ```
 * ────────────────────────────────────────────────────────────────────────────
 *
 * @param minActiveState Minimum lifecycle state at which events are collected.
 *                       Defaults to [Lifecycle.State.STARTED] (safe for UI).
 * @param onEvent        Lambda invoked for each emitted [ViewSingleEvent].
 *                       Runs on the main dispatcher inside [LaunchedEffect].
 */
@Composable
fun <SE : ViewSingleEvent> Flow<SE>.collectSingleEvent(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    onEvent: suspend (SE) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    // Keep a reference to the *latest* onEvent lambda without restarting the
    // coroutine every time the parent recomposes with a new lambda instance.
    val currentOnEvent by rememberUpdatedState(onEvent)

    LaunchedEffect(this, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            this@collectSingleEvent.collect { event ->
                currentOnEvent(event)
            }
        }
    }
}

