package com.example.natmusic.feature.home.data.repository

import com.example.natmusic.feature.home.data.mapper.toDomain
import com.example.natmusic.feature.home.data.source.local.MockMusicDataSource
import com.example.natmusic.feature.home.domain.model.LibraryItem
import com.example.natmusic.feature.home.domain.model.LibraryItemType
import com.example.natmusic.feature.home.domain.repository.LibraryRepository

class LibraryRepositoryImpl : LibraryRepository {

    private val items by lazy {
        MockMusicDataSource.libraryItems().map { it.toDomain() }
    }

    override suspend fun getLibraryItems(): List<LibraryItem> = items

    override suspend fun getLibraryItemsByType(type: LibraryItemType?): List<LibraryItem> =
        if (type == null) items else items.filter { it.type == type }
}
