package com.aldeanapps.routinapp.presentation.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.aldeanapps.routinapp.domain.model.WellnessSession
import com.aldeanapps.routinapp.presentation.common.FilterRow
import com.aldeanapps.routinapp.presentation.common.theme.Size4
import com.aldeanapps.routinapp.presentation.common.theme.Size8
import com.aldeanapps.routinapp.presentation.common.theme.Size12
import com.aldeanapps.routinapp.presentation.common.theme.Size16
import com.aldeanapps.routinapp.presentation.common.theme.Size32
import com.aldeanapps.routinapp.presentation.common.theme.Size100

/**
 * Favorites screen displaying favorited wellness sessions.
 */
@Composable
fun FavoritesScreen(
    searchQuery: String = "",
    onNavigateToDetail: (Int) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val favoriteSessions by viewModel.favoriteSessions.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()

    LaunchedEffect(searchQuery) {
        viewModel.onSearchQueryChange(searchQuery)
    }
    
    Column(modifier = Modifier.fillMaxSize()) {
        FilterRow(
            resultsCount = favoriteSessions.size,
            selectedCategory = selectedCategory,
            categories = viewModel.categories,
            onCategorySelected = { viewModel.onCategorySelected(it) }
        )
        
        if (favoriteSessions.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {
                EmptyFavoritesState(modifier = Modifier.fillMaxSize())
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(Size16),
                verticalArrangement = Arrangement.spacedBy(Size12),
                modifier = Modifier.fillMaxSize()
            ) {
                items(favoriteSessions, key = { it.id }) { session ->
                    FavoriteSessionCard(
                        session = session,
                        onClick = { onNavigateToDetail(session.id) },
                        onFavoriteClick = { viewModel.toggleFavorite(session.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoriteSessionCard(
    session: WellnessSession,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = Size4)
    ) {
        Row(
            modifier = Modifier.padding(Size12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = session.imageUrl,
                contentDescription = session.title,
                modifier = Modifier
                    .size(Size100)
                    .clip(RoundedCornerShape(Size12)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(Size12))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = session.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(Size4))
                
                Text(
                    text = session.category,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(Size8))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(Size16)
                    )
                    Spacer(modifier = Modifier.width(Size4))
                    Text(
                        text = session.rating.toString(),
                        style = MaterialTheme.typography.bodySmall
                    )
                    
                    Spacer(modifier = Modifier.width(Size16))
                    
                    Text(
                        text = "${session.duration} min",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Remove from favorites",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun EmptyFavoritesState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(Size32),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "❤️",
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.height(Size16))
        Text(
            text = "No Favorites Yet",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(Size8))
        Text(
            text = "Start adding sessions to your favorites",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
