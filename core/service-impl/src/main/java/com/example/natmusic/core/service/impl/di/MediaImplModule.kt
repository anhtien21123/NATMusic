package com.example.natmusic.core.service.impl.di

import com.example.natmusic.core.service.api.MusicController
import com.example.natmusic.core.service.impl.MusicControllerImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Internal Koin module for media implementation.
 */
val mediaImplModule = module {
    single<MusicController> {
        MusicControllerImpl(androidContext())
    }
}
