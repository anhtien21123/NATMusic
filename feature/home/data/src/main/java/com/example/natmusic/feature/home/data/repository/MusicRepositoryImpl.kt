package com.example.natmusic.feature.home.data.repository

import com.example.natmusic.feature.home.data.mapper.toDomain
import com.example.natmusic.feature.home.data.source.local.MockMusicDataSource
import com.example.natmusic.feature.home.domain.model.HomeFeed
import com.example.natmusic.feature.home.domain.model.Track
import com.example.natmusic.feature.home.domain.repository.MusicRepository

class MusicRepositoryImpl : MusicRepository {

    override suspend fun getAllTracks(): List<Track> =
        MockMusicDataSource.tracks().map { it.toDomain() }

    override suspend fun getTrackById(id: String): Track? =
        getAllTracks().find { it.id == id }

    override suspend fun getHomeFeed(): HomeFeed {
        val tracks = getAllTracks()
        return HomeFeed(
            recent = tracks.shuffled(),
            recommended = tracks.shuffled(),
            trending = tracks.shuffled()
        )
    }
}
