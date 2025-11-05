package com.aldeanapps.routinapp.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldeanapps.routinapp.domain.model.WellnessSession
import com.aldeanapps.routinapp.domain.usecase.GetSessionByIdUseCase
import com.aldeanapps.routinapp.domain.usecase.ToggleFavoriteUseCase
import com.aldeanapps.routinapp.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the session detail screen.
 */
@HiltViewModel
class SessionDetailViewModel @Inject constructor(
    private val getSessionByIdUseCase: GetSessionByIdUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val sessionId: Int = checkNotNull(savedStateHandle["sessionId"])
    
    private val _uiState = MutableStateFlow<UiState<WellnessSession>>(UiState.Loading)
    val uiState: StateFlow<UiState<WellnessSession>> = _uiState.asStateFlow()
    
    init {
        loadSession()
    }
    
    private fun loadSession() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            
            getSessionByIdUseCase(sessionId).fold(
                onSuccess = { session ->
                    _uiState.value = UiState.Success(session)
                },
                onFailure = { error ->
                    _uiState.value = UiState.Error(
                        error.message ?: "Failed to load session details"
                    )
                }
            )
        }
    }
    
    fun toggleFavorite() {
        viewModelScope.launch {
            toggleFavoriteUseCase(sessionId).onSuccess { isFavorite ->
                val currentState = _uiState.value
                if (currentState is UiState.Success) {
                    _uiState.value = UiState.Success(
                        currentState.data.copy(isFavorite = isFavorite)
                    )
                }
            }
        }
    }
}
