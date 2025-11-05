package com.aldeanapps.routinapp.domain.usecase

import com.aldeanapps.routinapp.domain.model.WellnessSession
import com.aldeanapps.routinapp.domain.repository.WellnessRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for fetching favorite sessions as a Flow.
 */
class GetFavoriteSessionsUseCase @Inject constructor(
    private val repository: WellnessRepository
) {
    operator fun invoke(): Flow<List<WellnessSession>> {
        return repository.getFavoriteSessions()
    }
}
