package com.aldeanapps.routinapp.domain.repository

import com.aldeanapps.routinapp.domain.model.WellnessSession
import kotlinx.coroutines.flow.Flow

interface WellnessRepository {

    suspend fun getSessions(): Result<List<WellnessSession>>
    suspend fun getSessionsByCategory(category: String): Result<List<WellnessSession>>
    suspend fun getSessionById(id: Int): Result<WellnessSession>
    suspend fun toggleFavorite(sessionId: Int): Result<Boolean>
    fun getFavoriteSessions(): Flow<List<WellnessSession>>
}
