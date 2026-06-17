package com.example.natmusic.feature.home.domain.model

data class Track(
    val id: String,
    val title: String,
    val artist: String,
    val imageUrl: String,
    val audioUrl: String
)

data class HomeFeed(
    val recent: List<Track>,
    val recommended: List<Track>,
    val trending: List<Track>
)

enum class LibraryItemType {
    PLAYLIST, ARTIST, ALBUM
}

data class LibraryItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val type: LibraryItemType
)

data class ExploreCategory(
    val id: String,
    val name: String,
    val colorArgb: Long
)

data class PlaybackState(
    val isPlaying: Boolean = false,
    val currentTrackId: String? = null,
    val progress: Float = 0f
)

enum class SkipDirection {
    NEXT, PREVIOUS
}
