package com.example.natmusic.feature.home.explore

import androidx.lifecycle.viewModelScope
import com.example.natmusic.core.mvi.BaseViewModel
import com.example.natmusic.feature.home.domain.usecase.GetExploreCategoriesUseCase
import kotlinx.coroutines.launch

class ExploreViewModel(
    private val getExploreCategories: GetExploreCategoriesUseCase
) : BaseViewModel<
    ExploreContract.State,
    ExploreContract.Intent,
    ExploreContract.SingleEvent
>(
    initialState = ExploreContract.State()
) {
    init {
        loadCategories()
    }

    override fun handleIntent(intent: ExploreContract.Intent) {
        when (intent) {
            is ExploreContract.Intent.OpenCategory ->
                sendSingleEvent(ExploreContract.SingleEvent.NavigateToCategory(intent.id))

            is ExploreContract.Intent.Search ->
                updateState { copy(searchQuery = intent.query) }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            val categories = getExploreCategories()
            updateState {
                copy(isLoading = false, categories = categories.map { it.toUi() })
            }
        }
    }
}
