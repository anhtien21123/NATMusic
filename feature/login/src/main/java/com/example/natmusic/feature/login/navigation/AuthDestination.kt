package com.example.natmusic.feature.login.navigation

import com.example.natmusic.core.navigation.AuthDestination
import com.example.natmusic.core.navigation.AuthNavigationContract

/**
 * Concrete implementation of the shared [AuthDestination] for the login module.
 */
data object Login : AuthDestination

/**
 * Implementation of [AuthNavigationContract] that allows other modules to navigate
 * to destinations inside the login feature.
 */
object AuthNavigationContractImpl : AuthNavigationContract {
    override val login: AuthDestination = Login
}
