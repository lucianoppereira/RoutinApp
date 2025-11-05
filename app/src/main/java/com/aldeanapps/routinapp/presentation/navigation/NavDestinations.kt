package com.aldeanapps.routinapp.presentation.navigation

import kotlinx.serialization.Serializable

open class NavDestinations {
    override fun equals(other: Any?) = other === this
    override fun hashCode() = this::class.hashCode()
    override fun toString() = this::class.simpleName!!
}

@Serializable
object SplashScreen : NavDestinations()

@Serializable
object SessionsScreen : NavDestinations()

@Serializable
object FavoritesScreen : NavDestinations()

@Serializable
data class SessionDetailScreen(val sessionId: Int) : NavDestinations()