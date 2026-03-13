package com.example.natmusic.feature.home.detail

import com.example.natmusic.core.mvi.BaseViewModel

/**
 * ViewModel for the Detail screen. Scoped to the individual NavEntry.
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
