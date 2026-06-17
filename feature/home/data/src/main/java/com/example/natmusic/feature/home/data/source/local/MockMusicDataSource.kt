package com.example.natmusic.feature.home.data.source.local

internal object MockMusicDataSource {

    fun tracks() = listOf(
        TrackRecord(
            id = "1",
            title = "Midnight Rain",
            artist = "Taylor Swift",
            imageUrl = "https://picsum.photos/seed/1/300/300",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
        ),
        TrackRecord(
            id = "2",
            title = "As It Was",
            artist = "Harry Styles",
            imageUrl = "https://picsum.photos/seed/2/300/300",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"
        ),
        TrackRecord(
            id = "3",
            title = "Anti-Hero",
            artist = "Taylor Swift",
            imageUrl = "https://picsum.photos/seed/3/300/300",
            audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3"
        )
    )

    fun libraryItems() = listOf(
        LibraryRecord("1", "Liked Songs", "Playlist • 120 songs", "https://picsum.photos/seed/liked/300/300", "PLAYLIST"),
        LibraryRecord("2", "Taylor Swift", "Artist", "https://picsum.photos/seed/taylor/300/300", "ARTIST"),
        LibraryRecord("3", "Midnights", "Album • Taylor Swift", "https://picsum.photos/seed/midnights/300/300", "ALBUM"),
        LibraryRecord("4", "Road Trip", "Playlist", "https://picsum.photos/seed/road/300/300", "PLAYLIST"),
        LibraryRecord("5", "Harry's House", "Album • Harry Styles", "https://picsum.photos/seed/harry/300/300", "ALBUM"),
        LibraryRecord("6", "Ed Sheeran", "Artist", "https://picsum.photos/seed/ed/300/300", "ARTIST"),
        LibraryRecord("7", "Chill Vibes", "Playlist", "https://picsum.photos/seed/chill/300/300", "PLAYLIST"),
        LibraryRecord("8", "Workout Mix", "Playlist", "https://picsum.photos/seed/workout/300/300", "PLAYLIST")
    )

    fun exploreCategories() = listOf(
        CategoryRecord("1", "Pop", 0xFFEF5350),
        CategoryRecord("2", "Rock", 0xFFAB47BC),
        CategoryRecord("3", "Hip-Hop", 0xFF42A5F5),
        CategoryRecord("4", "Jazz", 0xFF26A69A),
        CategoryRecord("5", "Electronic", 0xFF66BB6A),
        CategoryRecord("6", "R&B", 0xFFFFA726),
        CategoryRecord("7", "Classic", 0xFF8D6E63),
        CategoryRecord("8", "Country", 0xFF78909C),
        CategoryRecord("9", "Folk", 0xFF5C6BC0),
        CategoryRecord("10", "Blues", 0xFF26C6DA)
    )

    internal data class TrackRecord(
        val id: String,
        val title: String,
        val artist: String,
        val imageUrl: String,
        val audioUrl: String
    )

    internal data class LibraryRecord(
        val id: String,
        val title: String,
        val subtitle: String,
        val imageUrl: String,
        val type: String
    )

    internal data class CategoryRecord(
        val id: String,
        val name: String,
        val colorArgb: Long
    )
}
