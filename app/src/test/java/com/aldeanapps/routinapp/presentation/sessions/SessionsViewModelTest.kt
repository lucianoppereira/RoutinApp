package com.aldeanapps.routinapp.presentation.sessions

import com.aldeanapps.routinapp.domain.model.WellnessSession
import com.aldeanapps.routinapp.domain.usecase.GetSessionsUseCase
import com.aldeanapps.routinapp.domain.usecase.ToggleFavoriteUseCase
import com.aldeanapps.routinapp.presentation.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Unit tests for SessionsViewModel.
 * Demonstrates testing ViewModels with coroutines and StateFlow.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class SessionsViewModelTest {
    
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getSessionsUseCase: GetSessionsUseCase
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase
    private lateinit var viewModel: SessionsViewModel
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getSessionsUseCase = mock()
        toggleFavoriteUseCase = mock()
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `initial state is Loading`() = runTest {
        // Given
        whenever(getSessionsUseCase()).thenReturn(Result.success(emptyList()))
        
        // When
        viewModel = SessionsViewModel(getSessionsUseCase, toggleFavoriteUseCase)
        
        // Then - before advancing idle, state should be Loading
        assertTrue(viewModel.uiState.value is UiState.Loading)
    }
    
    @Test
    fun `loadSessions updates state to Success when successful`() = runTest {
        // Given
        val sessions = listOf(
            WellnessSession(
                id = "1",
                title = "Yoga",
                category = "Wellness",
                duration = 30,
                rating = 4.5,
                imageUrl = "",
                description = "Yoga session",
                instructor = "John",
                date = "2024-01-01",
                isFavorite = false
            )
        )
        whenever(getSessionsUseCase()).thenReturn(Result.success(sessions))
        
        // When
        viewModel = SessionsViewModel(getSessionsUseCase, toggleFavoriteUseCase)
        advanceUntilIdle() // Wait for coroutines to complete
        
        // Then
        val state = viewModel.uiState.value
        assertTrue(state is UiState.Success)
        assertEquals(sessions, (state as UiState.Success).data)
    }
    
    @Test
    fun `loadSessions updates state to Error when failed`() = runTest {
        // Given
        val errorMessage = "Network error"
        whenever(getSessionsUseCase()).thenReturn(
            Result.failure(Exception(errorMessage))
        )
        
        // When
        viewModel = SessionsViewModel(getSessionsUseCase, toggleFavoriteUseCase)
        advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertTrue(state is UiState.Error)
        assertEquals(errorMessage, (state as UiState.Error).message)
    }
}
