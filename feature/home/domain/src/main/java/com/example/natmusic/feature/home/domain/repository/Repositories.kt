package com.example.natmusic.feature.home.domain.repository

import com.example.natmusic.feature.home.domain.model.ExploreCategory
import com.example.natmusic.feature.home.domain.model.HomeFeed
import com.example.natmusic.feature.home.domain.model.LibraryItem
import com.example.natmusic.feature.home.domain.model.LibraryItemType
import com.example.natmusic.feature.home.domain.model.PlaybackState
import com.example.natmusic.feature.home.domain.model.SkipDirection
import com.example.natmusic.feature.home.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    suspend fun getAllTracks(): List<Track>
    suspend fun getTrackById(id: String): Track?
    suspend fun getHomeFeed(): HomeFeed
}

interface LibraryRepository {
    suspend fun getLibraryItems(): List<LibraryItem>
    suspend fun getLibraryItemsByType(type: LibraryItemType?): List<LibraryItem>
}

interface ExploreRepository {
    suspend fun getCategories(): List<ExploreCategory>
}

interface PlaybackRepository {
    val playbackState: Flow<PlaybackState>
    suspend fun play(trackId: String)
    fun pause()
    fun resume()
    fun seekTo(progress: Float)
    suspend fun skip(direction: SkipDirection)
}
