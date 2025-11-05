package com.aldeanapps.routinapp.data.repository

import com.aldeanapps.routinapp.data.local.preferences.PreferencesManager
import com.aldeanapps.routinapp.data.remote.api.WellnessApiService
import com.aldeanapps.routinapp.data.remote.dto.toDomain
import com.aldeanapps.routinapp.domain.model.WellnessSession
import com.aldeanapps.routinapp.domain.repository.WellnessRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of WellnessRepository.
 * Handles data operations and coordinates between API and local storage.
 * 
 * @Singleton ensures single instance for cache management.
 */
@Singleton
class WellnessRepositoryImpl @Inject constructor(
    private val apiService: WellnessApiService,
    private val preferencesManager: PreferencesManager
) : WellnessRepository {
    
    private val sessionsCache = MutableStateFlow<List<WellnessSession>>(emptyList())
    
    override suspend fun getSessions(): Result<List<WellnessSession>> {
        return try {
            val response = apiService.getSessions()
            if (response.isSuccessful && response.body() != null) {
                val favoriteIds = preferencesManager.getFavoriteIds()
                val sessions = response.body()!!.map { dto ->
                    dto.toDomain(isFavorite = favoriteIds.contains(dto.id))
                }
                sessionsCache.value = sessions
                Result.success(sessions)
            } else {
                Result.failure(Exception("Failed to fetch sessions: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSessionsByCategory(category: String): Result<List<WellnessSession>> {
        return try {
            val response = apiService.getSessionsByCategory(category)
            if (response.isSuccessful && response.body() != null) {
                val favoriteIds = preferencesManager.getFavoriteIds()
                val sessions = response.body()!!.map { dto ->
                    dto.toDomain(isFavorite = favoriteIds.contains(dto.id))
                }
                sessionsCache.value = sessions
                Result.success(sessions)
            } else {
                Result.failure(Exception("Failed to fetch sessions: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getSessionById(id: Int): Result<WellnessSession> {
        return try {
            // First check cache
            val cachedSession = sessionsCache.value.find { it.id == id }
            if (cachedSession != null) {
                return Result.success(cachedSession)
            }
            
            // If not in cache, fetch from API
            val response = apiService.getSessionById(id)
            if (response.isSuccessful && response.body() != null) {
                val session = response.body()!!.toDomain(
                    isFavorite = preferencesManager.isFavorite(id)
                )
                Result.success(session)
            } else {
                Result.failure(Exception("Failed to fetch session: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun toggleFavorite(sessionId: Int): Result<Boolean> {
        return try {
            val isFavorite = preferencesManager.toggleFavorite(sessionId)
            
            // Update cache
            val updatedSessions = sessionsCache.value.map { session ->
                if (session.id == sessionId) {
                    session.copy(isFavorite = isFavorite)
                } else {
                    session
                }
            }
            sessionsCache.value = updatedSessions
            
            Result.success(isFavorite)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun getFavoriteSessions(): Flow<List<WellnessSession>> {
        return sessionsCache.map { sessions ->
            sessions.filter { it.isFavorite }
        }
    }
}
