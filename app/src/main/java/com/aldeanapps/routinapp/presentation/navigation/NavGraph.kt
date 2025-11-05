package com.aldeanapps.routinapp.presentation.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.aldeanapps.routinapp.presentation.detail.SessionDetailScreen
import com.aldeanapps.routinapp.presentation.favorites.FavoritesScreen
import com.aldeanapps.routinapp.presentation.favorites.FavoritesViewModel
import com.aldeanapps.routinapp.presentation.sessions.SessionsScreen
import com.aldeanapps.routinapp.presentation.splash.SplashScreen

/**
 * Main navigation graph for the app.
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: NavDestinations = SplashScreen,
    paddingValues: PaddingValues = PaddingValues(),
    searchQuery: String = "",
) {
    NavHost(
        navController = navController,
        startDestination = startDestination::class.qualifiedName ?: "",
        modifier = Modifier.padding(paddingValues)
    ) {
        composable<SplashScreen> {
            SplashScreen(
                onNavigateToMain = {
                    navController.navigate(SessionsScreen) {
                        popUpTo(SplashScreen) { inclusive = true }
                    }
                }
            )
        }
        
        composable<SessionsScreen>(
            enterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(450)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(450)) },
        ) {
            SessionsScreen(
                searchQuery = searchQuery,
                onNavigateToDetail = { sessionId ->
                    navController.navigate(SessionDetailScreen(sessionId))
                }
            )
        }
        
        composable<FavoritesScreen>(
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(450)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(450)) }
        ) {
            FavoritesScreen(
                searchQuery = searchQuery,
                onNavigateToDetail = { sessionId ->
                    navController.navigate(SessionDetailScreen(sessionId))
                }
            )
        }
        
        composable<SessionDetailScreen> { backStackEntry ->
            val args: SessionDetailScreen = backStackEntry.toRoute()
            SessionDetailScreen(
                sessionId = args.sessionId,
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}
