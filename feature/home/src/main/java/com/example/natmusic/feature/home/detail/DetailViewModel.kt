package com.example.natmusic.feature.home.detail

import androidx.lifecycle.viewModelScope
import com.example.natmusic.core.mvi.BaseViewModel
import com.example.natmusic.feature.home.domain.usecase.GetTrackByIdUseCase
import kotlinx.coroutines.launch

class DetailViewModel(
    private val id: String,
    private val origin: String,
    private val getTrackById: GetTrackByIdUseCase
) : BaseViewModel<DetailContract.State, DetailContract.Intent, DetailContract.SingleEvent>(
    initialState = DetailContract.State(id = id, origin = origin)
) {
    init {
        loadDetail()
    }

    private fun loadDetail() {
        viewModelScope.launch {
            val track = getTrackById(id)
            updateState {
                copy(
                    title = track?.title ?: "Track #$id",
                    isLoading = false
                )
            }
        }
    }

    override fun handleIntent(intent: DetailContract.Intent) {
        when (intent) {
            DetailContract.Intent.OnBackClicked ->
                sendSingleEvent(DetailContract.SingleEvent.NavigateBack)
        }
    }
}
