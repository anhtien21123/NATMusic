package com.example.natmusic.feature.home.data.mapper

import com.example.natmusic.feature.home.data.source.local.MockMusicDataSource
import com.example.natmusic.feature.home.domain.model.ExploreCategory
import com.example.natmusic.feature.home.domain.model.LibraryItem
import com.example.natmusic.feature.home.domain.model.LibraryItemType
import com.example.natmusic.feature.home.domain.model.Track

internal fun MockMusicDataSource.TrackRecord.toDomain(): Track = Track(
    id = id,
    title = title,
    artist = artist,
    imageUrl = imageUrl,
    audioUrl = audioUrl
)

internal fun MockMusicDataSource.LibraryRecord.toDomain(): LibraryItem = LibraryItem(
    id = id,
    title = title,
    subtitle = subtitle,
    imageUrl = imageUrl,
    type = when (type) {
        "ARTIST" -> LibraryItemType.ARTIST
        "ALBUM" -> LibraryItemType.ALBUM
        else -> LibraryItemType.PLAYLIST
    }
)

internal fun MockMusicDataSource.CategoryRecord.toDomain(): ExploreCategory = ExploreCategory(
    id = id,
    name = name,
    colorArgb = colorArgb
)
