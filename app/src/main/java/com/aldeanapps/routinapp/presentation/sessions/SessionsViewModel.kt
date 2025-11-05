package com.aldeanapps.routinapp.presentation.sessions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldeanapps.routinapp.domain.model.Category
import com.aldeanapps.routinapp.domain.model.WellnessSession
import com.aldeanapps.routinapp.domain.usecase.GetSessionsUseCase
import com.aldeanapps.routinapp.domain.usecase.ToggleFavoriteUseCase
import com.aldeanapps.routinapp.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the sessions list screen.
 * Handles business logic and state management for the UI.
 */
@HiltViewModel
class SessionsViewModel @Inject constructor(
    private val getSessionsUseCase: GetSessionsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<UiState<List<WellnessSession>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<WellnessSession>>> = _uiState.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()
    
    private var allSessions: List<WellnessSession> = emptyList()
    
    val categories: List<String> = Category.getDisplayNames()
    
    init {
        loadSessions()
    }
    
    fun loadSessions() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            
            getSessionsUseCase().fold(
                onSuccess = { sessions ->
                    allSessions = sessions
                    applySearch()
                },
                onFailure = { error ->
                    _uiState.value = UiState.Error(
                        error.message ?: "Failed to load sessions"
                    )
                }
            )
        }
    }
    
    fun toggleFavorite(sessionId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(sessionId).onSuccess { isFavorite ->
                allSessions = allSessions.map { session ->
                    if (session.id == sessionId) {
                        session.copy(isFavorite = isFavorite)
                    } else {
                        session
                    }
                }
                applySearch()
            }
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
        
        var filtered = allSessions
        
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
        
        _uiState.value = UiState.Success(filtered)
    }
}
