package com.example.natmusic.feature.home.home

import com.example.natmusic.feature.home.domain.model.Track

data class MusicItemUi(
    val id: String,
    val title: String,
    val subtitle: String,
    val imageUrl: String
)

fun Track.toUi(): MusicItemUi = MusicItemUi(
    id = id,
    title = title,
    subtitle = artist,
    imageUrl = imageUrl
)
