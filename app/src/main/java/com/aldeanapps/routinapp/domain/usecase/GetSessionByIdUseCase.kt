package com.aldeanapps.routinapp.domain.usecase

import com.aldeanapps.routinapp.domain.model.WellnessSession
import com.aldeanapps.routinapp.domain.repository.WellnessRepository
import javax.inject.Inject

/**
 * Use case for fetching a single session by ID.
 */
class GetSessionByIdUseCase @Inject constructor(
    private val repository: WellnessRepository
) {
    suspend operator fun invoke(sessionId: Int): Result<WellnessSession> {
        return repository.getSessionById(sessionId)
    }
}
