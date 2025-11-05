package com.aldeanapps.routinapp.presentation.main

import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aldeanapps.routinapp.presentation.common.SearchBar
import com.aldeanapps.routinapp.presentation.navigation.NavGraph
import com.aldeanapps.routinapp.presentation.navigation.Screen

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
    
    // Show bottom navigation only on specific screens
    val showBottomBar = currentDestination?.route in listOf(
        Screen.Sessions.route,
        Screen.Favorites.route
    )
    
    // Show top app bar only on specific screens
    val showTopBar = currentDestination?.route in listOf(
        Screen.Sessions.route,
        Screen.Favorites.route
    )
    
    Scaffold(
        topBar = {
            if (showTopBar) {
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
                                text = when (currentDestination?.route) {
                                    Screen.Sessions.route -> "Wellness Sessions"
                                    Screen.Favorites.route -> "My Favorites"
                                    else -> "RoutinApp"
                                },
                                fontWeight = FontWeight.Bold
                            )
                        },
                        actions = {
                            IconButton(onClick = { isSearchActive = true }) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
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
            if (showBottomBar) {
                BottomNavigationBar(
                    navController = navController,
                    currentDestination = currentDestination?.route
                )
            }
        }
    ) { paddingValues ->
        NavGraph(
            navController = navController,
            startDestination = Screen.Splash.route,
            paddingValues = paddingValues,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it }
        )
    }
}

@Composable
private fun BottomNavigationBar(
    navController: NavHostController,
    currentDestination: String?
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Sessions") },
            label = { Text("Sessions") },
            selected = currentDestination == Screen.Sessions.route,
            onClick = {
                // If on Favorites, just pop back to Sessions
                if (currentDestination == Screen.Favorites.route) {
                    navController.popBackStack()
                } else if (currentDestination != Screen.Sessions.route) {
                    // Navigate to Sessions and clear back stack to it
                    navController.navigate(Screen.Sessions.route) {
                        popUpTo(Screen.Sessions.route) {
                            inclusive = true
                        }
                    }
                }
            }
        )
        
        NavigationBarItem(
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
            label = { Text("Favorites") },
            selected = currentDestination == Screen.Favorites.route,
            onClick = {
                // Only navigate if not already on Favorites
                if (currentDestination != Screen.Favorites.route) {
                    navController.navigate(Screen.Favorites.route) {
                        // Keep Sessions as the only item below Favorites
                        popUpTo(Screen.Sessions.route) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            }
        )
    }
}
