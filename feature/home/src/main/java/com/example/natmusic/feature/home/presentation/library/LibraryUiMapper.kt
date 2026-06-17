package com.example.natmusic.feature.home.library

import com.example.natmusic.feature.home.domain.model.LibraryItem
import com.example.natmusic.feature.home.domain.model.LibraryItemType

data class LibraryItemUi(
    val id: String,
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val type: LibraryItemType
)

fun LibraryItem.toUi(): LibraryItemUi = LibraryItemUi(
    id = id,
    title = title,
    subtitle = subtitle,
    imageUrl = imageUrl,
    type = type
)
