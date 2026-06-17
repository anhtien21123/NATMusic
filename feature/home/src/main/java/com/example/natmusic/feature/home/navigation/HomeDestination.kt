package com.example.natmusic.feature.home.navigation

import com.example.natmusic.core.navigation.HomeDestination
import com.example.natmusic.core.navigation.HomeNavigationContract

/**
 * Concrete implementations of the shared [HomeDestination] for the home module.
 */
data object Main : HomeDestination

data class Detail(val id: String, val origin: String) : HomeDestination

/**
 * Implementation of [HomeNavigationContract] that allows other modules to navigate
 * to destinations inside the home feature.
 */
object HomeNavigationContractImpl : HomeNavigationContract {
    override val main: HomeDestination = Main
    override fun detail(id: String, origin: String): HomeDestination = Detail(id, origin)
}
