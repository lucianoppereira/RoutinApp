package com.aldeanapps.routinapp.presentation.navigation

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
import com.aldeanapps.routinapp.presentation.detail.SessionDetailScreen
import com.aldeanapps.routinapp.presentation.favorites.FavoritesScreen
import com.aldeanapps.routinapp.presentation.sessions.SessionsScreen
import com.aldeanapps.routinapp.presentation.splash.SplashScreen

/**
 * Main navigation graph for the app.
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route,
    paddingValues: PaddingValues = PaddingValues(),
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToMain = {
                    navController.navigate(Screen.Sessions.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Sessions.route) {
            val viewModel: com.aldeanapps.routinapp.presentation.sessions.SessionsViewModel = hiltViewModel()
            LaunchedEffect(searchQuery) {
                viewModel.onSearchQueryChange(searchQuery)
            }
            SessionsScreen(
                onNavigateToDetail = { sessionId ->
                    navController.navigate(Screen.Detail.createRoute(sessionId))
                },
                viewModel = viewModel
            )
        }
        
        composable(Screen.Favorites.route) {
            val viewModel: com.aldeanapps.routinapp.presentation.favorites.FavoritesViewModel = hiltViewModel()
            LaunchedEffect(searchQuery) {
                viewModel.onSearchQueryChange(searchQuery)
            }
            FavoritesScreen(
                onNavigateToDetail = { sessionId ->
                    navController.navigate(Screen.Detail.createRoute(sessionId))
                },
                viewModel = viewModel
            )
        }
        
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("sessionId") { type = NavType.IntType }
            )
        ) {
            SessionDetailScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}
