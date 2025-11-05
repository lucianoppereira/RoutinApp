package com.aldeanapps.routinapp.presentation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aldeanapps.routinapp.R
import com.aldeanapps.routinapp.presentation.common.SearchBar
import com.aldeanapps.routinapp.presentation.navigation.FavoritesScreen
import com.aldeanapps.routinapp.presentation.navigation.NavDestinations
import com.aldeanapps.routinapp.presentation.navigation.NavGraph
import com.aldeanapps.routinapp.presentation.navigation.SessionsScreen

/**
 * Main screen container with bottom navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val currentRoute = currentDestination?.route
    
    val sessionsRoute = SessionsScreen::class.qualifiedName
    val favoritesRoute = FavoritesScreen::class.qualifiedName

    val showBars = when (currentRoute) {
        sessionsRoute, favoritesRoute -> true
        else -> false
    }

    // Animate content padding for smooth resizing when bars appear/disappear
    val topBarHeight = 64.dp // Standard TopAppBar height
    val bottomBarHeight = 80.dp // Standard NavigationBar height

    val animatedTopPadding by animateDpAsState(
        targetValue = if (showBars) topBarHeight else 0.dp,
        label = "topPadding"
    )
    val animatedBottomPadding by animateDpAsState(
        targetValue = if (showBars) bottomBarHeight else 0.dp,
        label = "bottomPadding"
    )

    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = showBars,
                enter = slideInVertically(initialOffsetY = { -it }),
                exit = slideOutVertically(targetOffsetY = { -it })
            ) {
                if (isSearchActive) {
                    TopAppBar(
                        title = {
                            SearchBar(
                                query = searchQuery,
                                onQueryChange = { searchQuery = it },
                                onClose = {
                                    isSearchActive = false
                                    searchQuery = ""
                                }
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                } else {
                    TopAppBar(
                        title = {
                            Text(
                                text = when (currentRoute) {
                                    sessionsRoute -> stringResource(R.string.screen_title_sessions)
                                    favoritesRoute -> stringResource(R.string.screen_title_favorites)
                                    else -> stringResource(R.string.app_name)
                                },
                                fontWeight = FontWeight.Bold
                            )
                        },
                        actions = {
                            IconButton(onClick = { isSearchActive = true }) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = stringResource(R.string.field_hint),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = showBars,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                BottomNavigationBar(
                    navController = navController,
                    currentDestination = currentRoute,
                    sessionsDestination = SessionsScreen,
                    favoritesDestination = FavoritesScreen,
                    sessionsRoute = sessionsRoute,
                    favoritesRoute = favoritesRoute
                )
            }
        }
    ) { scaffoldPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(
                    top = animatedTopPadding,
                    bottom = animatedBottomPadding,
                    start = scaffoldPadding.calculateLeftPadding(LocalLayoutDirection.current),
                    end = scaffoldPadding.calculateRightPadding(LocalLayoutDirection.current)
                )
        ) {
            NavGraph(
                navController = navController,
                paddingValues = PaddingValues(0.dp),
                searchQuery = searchQuery,
            )

            LaunchedEffect(navController) {
                if (navController.currentDestination == null) {
                    navController.navigate(SessionsScreen) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomNavigationBar(
    navController: NavHostController,
    currentDestination: String?,
    sessionsDestination: NavDestinations,
    favoritesDestination: NavDestinations,
    sessionsRoute: String? = sessionsDestination::class.qualifiedName,
    favoritesRoute: String? = favoritesDestination::class.qualifiedName
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Sessions") },
            label = { Text("Sessions") },
            selected = currentDestination == sessionsRoute,
            onClick = {
                if (currentDestination == favoritesRoute) {
                    navController.popBackStack()
                } else if (currentDestination != sessionsRoute) {
                    navController.navigate(SessionsScreen) {
                        popUpTo(sessionsDestination) {
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )
        
        NavigationBarItem(
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
            label = { Text("Favorites") },
            selected = currentDestination == favoritesRoute,
            onClick = {
                if (currentDestination != favoritesRoute) {
                    navController.navigate(FavoritesScreen) {
                        popUpTo(sessionsDestination) {
                            inclusive = false
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )
    }
}
