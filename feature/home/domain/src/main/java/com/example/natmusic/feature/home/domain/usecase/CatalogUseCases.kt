package com.example.natmusic.feature.home.domain.usecase

import com.example.natmusic.feature.home.domain.model.ExploreCategory
import com.example.natmusic.feature.home.domain.model.LibraryItem
import com.example.natmusic.feature.home.domain.model.LibraryItemType
import com.example.natmusic.feature.home.domain.repository.ExploreRepository
import com.example.natmusic.feature.home.domain.repository.LibraryRepository

class GetLibraryItemsUseCase(
    private val libraryRepository: LibraryRepository
) {
    suspend operator fun invoke(type: LibraryItemType? = null): List<LibraryItem> =
        if (type == null) libraryRepository.getLibraryItems()
        else libraryRepository.getLibraryItemsByType(type)
}

class GetExploreCategoriesUseCase(
    private val exploreRepository: ExploreRepository
) {
    suspend operator fun invoke(): List<ExploreCategory> = exploreRepository.getCategories()
}
