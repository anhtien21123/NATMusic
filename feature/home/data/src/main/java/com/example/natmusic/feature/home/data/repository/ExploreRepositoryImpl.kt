package com.example.natmusic.feature.home.data.repository

import com.example.natmusic.feature.home.data.mapper.toDomain
import com.example.natmusic.feature.home.data.source.local.MockMusicDataSource
import com.example.natmusic.feature.home.domain.model.ExploreCategory
import com.example.natmusic.feature.home.domain.repository.ExploreRepository

class ExploreRepositoryImpl : ExploreRepository {

    override suspend fun getCategories(): List<ExploreCategory> =
        MockMusicDataSource.exploreCategories().map { it.toDomain() }
}
