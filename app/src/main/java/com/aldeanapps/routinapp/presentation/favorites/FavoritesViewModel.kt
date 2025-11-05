package com.aldeanapps.routinapp.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldeanapps.routinapp.domain.model.Category
import com.aldeanapps.routinapp.domain.model.WellnessSession
import com.aldeanapps.routinapp.domain.usecase.GetFavoriteSessionsUseCase
import com.aldeanapps.routinapp.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the favorites screen.
 */
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoriteSessionsUseCase: GetFavoriteSessionsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {
    
    private val _favoriteSessions = MutableStateFlow<List<WellnessSession>>(emptyList())
    val favoriteSessions: StateFlow<List<WellnessSession>> = _favoriteSessions.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()
    
    private var allFavorites: List<WellnessSession> = emptyList()
    
    val categories: List<String> = Category.getDisplayNames()
    
    init {
        observeFavorites()
    }
    
    private fun observeFavorites() {
        viewModelScope.launch {
            getFavoriteSessionsUseCase().collect { favorites ->
                allFavorites = favorites
                applySearch()
            }
        }
    }
    
    fun toggleFavorite(sessionId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(sessionId)
        }
    }
    
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        applySearch()
    }
    
    fun toggleSearch() {
        _isSearchActive.value = !_isSearchActive.value
        if (!_isSearchActive.value) {
            _searchQuery.value = ""
            applySearch()
        }
    }
    
    fun onCategorySelected(category: String?) {
        _selectedCategory.value = category
        applySearch()
    }
    
    private fun applySearch() {
        val query = _searchQuery.value.trim()
        val category = _selectedCategory.value
        
        var filtered = allFavorites
        
        if (category != null) {
            filtered = filtered.filter { session ->
                session.category.equals(category, ignoreCase = true)
            }
        }
        
        if (query.isNotEmpty()) {
            filtered = filtered.filter { session ->
                session.title.contains(query, ignoreCase = true)
            }
        }
        
        _favoriteSessions.value = filtered
    }
}
