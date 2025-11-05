package com.aldeanapps.routinapp.domain.usecase

import com.aldeanapps.routinapp.domain.model.WellnessSession
import com.aldeanapps.routinapp.domain.repository.WellnessRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Unit tests for GetSessionsUseCase.
 * Demonstrates testing with mocks and coroutines.
 */
class GetSessionsUseCaseTest {
    
    private lateinit var repository: WellnessRepository
    private lateinit var useCase: GetSessionsUseCase
    
    @Before
    fun setup() {
        repository = mock()
        useCase = GetSessionsUseCase(repository)
    }
    
    @Test
    fun `invoke returns success when repository succeeds`() = runTest {
        // Given
        val expectedSessions = listOf(
            WellnessSession(
                id = "1",
                title = "Morning Yoga",
                category = "Yoga",
                duration = 45,
                rating = 4.8,
                imageUrl = "https://example.com/image.jpg",
                description = "Start your day right",
                instructor = "Jane Doe",
                date = "2024-01-15",
                isFavorite = false
            )
        )
        whenever(repository.getSessions()).thenReturn(Result.success(expectedSessions))
        
        // When
        val result = useCase()
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedSessions, result.getOrNull())
    }
    
    @Test
    fun `invoke returns failure when repository fails`() = runTest {
        // Given
        val exception = Exception("Network error")
        whenever(repository.getSessions()).thenReturn(Result.failure(exception))
        
        // When
        val result = useCase()
        
        // Then
        assertTrue(result.isFailure)
        assertEquals(exception.message, result.exceptionOrNull()?.message)
    }
}
