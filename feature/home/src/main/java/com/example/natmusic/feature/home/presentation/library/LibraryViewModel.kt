package com.example.natmusic.feature.home.library

import androidx.lifecycle.viewModelScope
import com.example.natmusic.core.mvi.BaseViewModel
import com.example.natmusic.feature.home.domain.usecase.GetLibraryItemsUseCase
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val getLibraryItems: GetLibraryItemsUseCase
) : BaseViewModel<
    LibraryContract.State,
    LibraryContract.Intent,
    LibraryContract.SingleEvent
>(
    initialState = LibraryContract.State()
) {
    init {
        loadItems()
    }

    override fun handleIntent(intent: LibraryContract.Intent) {
        when (intent) {
            is LibraryContract.Intent.OpenItem ->
                sendSingleEvent(LibraryContract.SingleEvent.NavigateToDetail(intent.id))

            is LibraryContract.Intent.FilterByType -> viewModelScope.launch {
                val filtered = getLibraryItems(intent.type)
                updateState {
                    copy(items = filtered.map { it.toUi() }, selectedFilter = intent.type)
                }
            }

            LibraryContract.Intent.OnSettingsClick ->
                sendSingleEvent(LibraryContract.SingleEvent.NavigateToSettings)
        }
    }

    private fun loadItems() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            val items = getLibraryItems()
            updateState { copy(isLoading = false, items = items.map { it.toUi() }) }
        }
    }
}
