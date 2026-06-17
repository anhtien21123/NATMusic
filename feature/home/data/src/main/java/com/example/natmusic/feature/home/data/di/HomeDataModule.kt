package com.example.natmusic.feature.home.data.di

import com.example.natmusic.feature.home.data.repository.ExploreRepositoryImpl
import com.example.natmusic.feature.home.data.repository.LibraryRepositoryImpl
import com.example.natmusic.feature.home.data.repository.MusicRepositoryImpl
import com.example.natmusic.feature.home.data.repository.PlaybackRepositoryImpl
import com.example.natmusic.feature.home.domain.repository.ExploreRepository
import com.example.natmusic.feature.home.domain.repository.LibraryRepository
import com.example.natmusic.feature.home.domain.repository.MusicRepository
import com.example.natmusic.feature.home.domain.repository.PlaybackRepository
import com.example.natmusic.feature.home.domain.usecase.GetExploreCategoriesUseCase
import com.example.natmusic.feature.home.domain.usecase.GetHomeFeedUseCase
import com.example.natmusic.feature.home.domain.usecase.GetLibraryItemsUseCase
import com.example.natmusic.feature.home.domain.usecase.GetTrackByIdUseCase
import com.example.natmusic.feature.home.domain.usecase.ObservePlaybackUseCase
import com.example.natmusic.feature.home.domain.usecase.PlayTrackUseCase
import com.example.natmusic.feature.home.domain.usecase.SeekPlaybackUseCase
import com.example.natmusic.feature.home.domain.usecase.SkipTrackUseCase
import com.example.natmusic.feature.home.domain.usecase.TogglePlaybackUseCase
import org.koin.dsl.module

val homeDataModule = module {
    single<MusicRepository> { MusicRepositoryImpl() }
    single<LibraryRepository> { LibraryRepositoryImpl() }
    single<ExploreRepository> { ExploreRepositoryImpl() }
    single<PlaybackRepository> { PlaybackRepositoryImpl(get(), get()) }

    factory { GetHomeFeedUseCase(get()) }
    factory { GetTrackByIdUseCase(get()) }
    factory { GetLibraryItemsUseCase(get()) }
    factory { GetExploreCategoriesUseCase(get()) }
    factory { ObservePlaybackUseCase(get()) }
    factory { PlayTrackUseCase(get()) }
    factory { TogglePlaybackUseCase(get()) }
    factory { SeekPlaybackUseCase(get()) }
    factory { SkipTrackUseCase(get()) }
}
