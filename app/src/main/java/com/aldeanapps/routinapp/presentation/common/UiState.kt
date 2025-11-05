package com.aldeanapps.routinapp.presentation.common

/**
 * Generic UI state wrapper for handling loading, success, and error states.
 * Follows the Open/Closed Principle - can be extended for different data types.
 */
sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
