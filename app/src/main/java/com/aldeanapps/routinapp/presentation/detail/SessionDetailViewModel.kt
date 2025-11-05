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
) : ViewModel() {
    
    private val _sessionId = MutableStateFlow(0)
    val sessionId: StateFlow<Int> = _sessionId
    
    private val _uiState = MutableStateFlow<UiState<WellnessSession>>(UiState.Loading)
    val uiState: StateFlow<UiState<WellnessSession>> = _uiState.asStateFlow()

    fun setSessionId(id: Int) {
        _sessionId.value = id
        loadSession()
    }
    
    private fun loadSession() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            
            getSessionByIdUseCase(_sessionId.value).fold(
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
            toggleFavoriteUseCase(_sessionId.value).onSuccess { isFavorite ->
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
