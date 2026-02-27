package com.example.natmusic.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.natmusic.core.common_ui.collectSingleEvent
import com.example.natmusic.core.mvi.BaseViewModel
import com.example.natmusic.core.mvi.ViewIntent
import com.example.natmusic.core.mvi.ViewSingleEvent
import com.example.natmusic.core.mvi.ViewState
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

// ─────────────────────────────────────────────────────────────────────────────
// MVI contract
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Detail screen MVI contract.
 * Demonstrates how a ViewModel receives route parameters and emits a
 * [SingleEvent] that triggers navigation back.
 */
object DetailContract {

    data class State(
        val id: String = "",
        val origin: String = "",
        val title: String = "",
        val isLoading: Boolean = true
    ) : ViewState

    sealed class Intent : ViewIntent {
        data object OnBackClicked : Intent()
    }

    sealed class SingleEvent : ViewSingleEvent {
        /** Emitted when the screen should be dismissed (back navigation). */
        data object NavigateBack : SingleEvent()
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// ViewModel
// ─────────────────────────────────────────────────────────────────────────────

/**
 * DetailViewModel — scoped to the Nav3 NavEntry for AppRoute.Detail.
 *
 * ── Koin scope in Nav3 ────────────────────────────────────────────────────────
 *
 *  Nav3's NavDisplay includes [rememberViewModelStoreNavEntryDecorator] by
 *  default (via lifecycle-viewmodel-navigation3).  Each entry gets its own
 *  ViewModelStoreOwner, so:
 *
 *   koinViewModel()           ← creates one instance per NavEntry (destination)
 *   koinViewModel { p }       ← passes constructor params (id, origin below)
 *
 *  When AppRoute.Detail is popped from the back stack, the entry's
 *  ViewModelStore is cleared → ViewModel.onCleared() fires automatically.
 *
 * ── Constructor parameters via Koin ──────────────────────────────────────────
 *
 *  The id/origin come from the typed AppRoute.Detail object, not from a Bundle.
 *  We pass them as Koin parameters:
 *
 *    koinViewModel { parametersOf(detail.id, detail.origin) }
 *
 *  The Koin module would be:
 *    factory { (id: String, origin: String) -> DetailViewModel(id, origin) }
 *
 *  This replaces the Nav2 pattern of reading from SavedStateHandle.
 */
class DetailViewModel(
    private val id: String,
    private val origin: String
) : BaseViewModel<DetailContract.State, DetailContract.Intent, DetailContract.SingleEvent>(
    initialState = DetailContract.State(id = id, origin = origin)
) {
    init {
        // Simulate loading detail data
        updateState {
            copy(
                title = "Track #$id",
                isLoading = false
            )
        }
    }

    override fun handleIntent(intent: DetailContract.Intent) {
        when (intent) {
            DetailContract.Intent.OnBackClicked ->
                sendSingleEvent(DetailContract.SingleEvent.NavigateBack)
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Screen
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Detail screen — demonstrates type-safe multi-param navigation with Nav3.
 *
 * ── How this screen is reached ────────────────────────────────────────────────
 *
 *  1. User taps a MusicItem in HomeScreen.
 *  2. HomeScreen calls:
 *       viewModel.handleIntent(HomeContract.Intent.OpenMusicItem(item.id))
 *  3. HomeViewModel sends:
 *       sendSingleEvent(HomeContract.SingleEvent.NavigateToDetail(item.id))
 *  4. HomeScreen's collectSingleEvent calls:
 *       onNavigateToDetail(event.id)
 *  5. HomeNavScreen passes this up via Intent.OnDetailRequested.
 *  6. HomeNavViewModel sends SingleEvent.NavigateToDetail(id, origin).
 *  7. HomeNavScreen calls the lambda from AppNavHost:
 *       onNavigateToDetail(id, origin)
 *  8. AppNavHost adds the typed route to the back stack:
 *       backStack.add(AppRoute.Detail(id = id, origin = origin))
 *  9. NavDisplay renders this screen with `detail.id` and `detail.origin`
 *     directly available as Kotlin properties — no Bundle, no string parsing.
 *
 * ── ViewModel injection ───────────────────────────────────────────────────────
 *
 *  koinViewModel { parametersOf(id, origin) } injects DetailViewModel with
 *  constructor parameters from the typed AppRoute.Detail object.
 *  The ViewModel is scoped to this Nav3 entry's ViewModelStore.
 *
 * ── SingleEvent → navigation ──────────────────────────────────────────────────
 *
 *  This screen demonstrates the FULL MVI navigation loop:
 *    Intent.OnBackClicked  →  ViewModel.handleIntent
 *                          →  sendSingleEvent(NavigateBack)
 *                          →  collectSingleEvent { onBack() }
 *                          →  AppNavHost: backStack.removeLastOrNull()
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    id: String,
    origin: String,
    onBack: () -> Unit,
    // Koin injects DetailViewModel and auto-resolves id + origin from parameters
    viewModel: DetailViewModel = koinViewModel { parametersOf(id, origin) }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // ── Lifecycle-aware SingleEvent collection ────────────────────────────────
    // collectSingleEvent is the extension from :core:common_ui.
    // It uses repeatOnLifecycle(STARTED) — events are never delivered while
    // the screen is in the back-stack (paused/stopped state).
    viewModel.singleEvent.collectSingleEvent { event ->
        when (event) {
            DetailContract.SingleEvent.NavigateBack -> onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = state.title.ifEmpty { "Loading…" })
                        Text(
                            text = "via $origin",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            // Intent flows through ViewModel so any business
                            // logic (analytics, etc.) can intercept it.
                            viewModel.handleIntent(DetailContract.Intent.OnBackClicked)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Track ID",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = id,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Navigation Origin",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = origin,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "ViewModel Scope",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Scoped to this Nav3 NavEntry — cleared when back-stack entry is popped.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

