package com.aldeanapps.routinapp.presentation.navigation

/**
 * Sealed class representing app navigation destinations.
 * Type-safe navigation routes.
 */
sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Sessions : Screen("sessions")
    data object Favorites : Screen("favorites")
    data object Detail : Screen("detail/{sessionId}") {
        fun createRoute(sessionId: Int) = "detail/$sessionId"
    }
}
