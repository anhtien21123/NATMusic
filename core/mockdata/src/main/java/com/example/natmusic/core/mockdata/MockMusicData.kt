package com.example.natmusic.core.mockdata

data class MockMusicItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val musicUrl: String
)

object MockData {
    val musicList = listOf(
        MockMusicItem(
            id = "1",
            title = "Midnight Rain",
            subtitle = "Taylor Swift",
            imageUrl = "https://picsum.photos/seed/1/300/300",
            musicUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
        ),
        MockMusicItem(
            id = "2",
            title = "As It Was",
            subtitle = "Harry Styles",
            imageUrl = "https://picsum.photos/seed/2/300/300",
            musicUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"
        ),
        MockMusicItem(
            id = "3",
            title = "Anti-Hero",
            subtitle = "Taylor Swift",
            imageUrl = "https://picsum.photos/seed/3/300/300",
            musicUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3"
        )
    )
}
