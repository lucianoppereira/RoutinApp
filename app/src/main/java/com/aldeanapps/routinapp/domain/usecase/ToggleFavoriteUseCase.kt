package com.aldeanapps.routinapp.domain.usecase

import com.aldeanapps.routinapp.domain.repository.WellnessRepository
import javax.inject.Inject

/**
 * Use case for toggling favorite status of a session.
 * Updates both persistent storage and in-memory cache.
 */
class ToggleFavoriteUseCase @Inject constructor(
    private val repository: WellnessRepository
) {
    suspend operator fun invoke(sessionId: Int): Result<Boolean> {
        return repository.toggleFavorite(sessionId)
    }
}
