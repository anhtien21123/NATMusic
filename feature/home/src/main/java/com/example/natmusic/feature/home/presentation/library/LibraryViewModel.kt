package com.example.natmusic.feature.home.library

import com.example.natmusic.core.mvi.BaseViewModel

class LibraryViewModel : BaseViewModel<
    LibraryContract.State,
    LibraryContract.Intent,
    LibraryContract.SingleEvent
>(
    initialState = LibraryContract.State()
) {
    private var allItems: List<MediaItem> = emptyList()

    init { loadMockData() }

    override fun handleIntent(intent: LibraryContract.Intent) {
        when (intent) {
            is LibraryContract.Intent.OpenItem ->
                sendSingleEvent(LibraryContract.SingleEvent.NavigateToDetail(intent.id))
            is LibraryContract.Intent.FilterByType -> {
                val filtered = if (intent.type == null) allItems
                               else allItems.filter { it.type == intent.type }
                updateState { copy(items = filtered, selectedFilter = intent.type) }
            }
            LibraryContract.Intent.OnSettingsClick ->
                sendSingleEvent(LibraryContract.SingleEvent.NavigateToSettings)
        }
    }

    private fun loadMockData() {
        allItems = listOf(
            MediaItem("1", "Liked Songs",   "Playlist • 120 songs",  "https://picsum.photos/seed/liked/300/300",     MediaType.PLAYLIST),
            MediaItem("2", "Taylor Swift",  "Artist",                "https://picsum.photos/seed/taylor/300/300",    MediaType.ARTIST),
            MediaItem("3", "Midnights",     "Album • Taylor Swift",  "https://picsum.photos/seed/midnights/300/300", MediaType.ALBUM),
            MediaItem("4", "Road Trip",     "Playlist",              "https://picsum.photos/seed/road/300/300",      MediaType.PLAYLIST),
            MediaItem("5", "Harry's House", "Album • Harry Styles",  "https://picsum.photos/seed/harry/300/300",     MediaType.ALBUM),
            MediaItem("6", "Ed Sheeran",    "Artist",                "https://picsum.photos/seed/ed/300/300",        MediaType.ARTIST),
            MediaItem("7", "Chill Vibes",   "Playlist",              "https://picsum.photos/seed/chill/300/300",     MediaType.PLAYLIST),
            MediaItem("8", "Workout Mix",   "Playlist",              "https://picsum.photos/seed/workout/300/300",   MediaType.PLAYLIST)
        )
        updateState { copy(items = allItems) }
    }
}

