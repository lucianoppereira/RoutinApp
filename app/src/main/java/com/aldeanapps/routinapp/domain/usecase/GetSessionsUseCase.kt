package com.aldeanapps.routinapp.domain.usecase

import com.aldeanapps.routinapp.domain.model.WellnessSession
import com.aldeanapps.routinapp.domain.repository.WellnessRepository
import javax.inject.Inject

/**
 * Use case for fetching wellness sessions.
 * Follows the Single Responsibility Principle - handles only session retrieval logic.
 */
class GetSessionsUseCase @Inject constructor(
    private val repository: WellnessRepository
) {
    suspend operator fun invoke(): Result<List<WellnessSession>> {
        return repository.getSessions()
    }
}
