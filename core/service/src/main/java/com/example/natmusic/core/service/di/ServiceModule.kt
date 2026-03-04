package com.example.natmusic.core.service.di

import androidx.media3.exoplayer.ExoPlayer
import com.example.natmusic.core.service.MusicPlayerHandler
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val serviceModule = module {
    single {
        ExoPlayer.Builder(androidContext())
            .build()
    }
    single {
        MusicPlayerHandler(androidContext())
    }
}
