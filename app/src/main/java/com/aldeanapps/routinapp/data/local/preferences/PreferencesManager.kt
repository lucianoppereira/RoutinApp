package com.aldeanapps.routinapp.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager for SharedPreferences operations.
 * Provides type-safe access to stored preferences.
 */
@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    /**
     * Save a set of favorite session IDs
     */
    fun saveFavoriteIds(favoriteIds: Set<Int>) {
        sharedPreferences.edit()
            .putStringSet(KEY_FAVORITE_IDS, favoriteIds.map { it.toString() }.toSet())
            .apply()
    }

    /**
     * Get the set of favorite session IDs
     */
    fun getFavoriteIds(): Set<Int> {
        return sharedPreferences.getStringSet(KEY_FAVORITE_IDS, emptySet())
            ?.mapNotNull { it.toIntOrNull() }
            ?.toSet()
            ?: emptySet()
    }

    /**
     * Add a session ID to favorites
     */
    fun addFavorite(sessionId: Int) {
        val currentFavorites = getFavoriteIds().toMutableSet()
        currentFavorites.add(sessionId)
        saveFavoriteIds(currentFavorites)
    }

    /**
     * Remove a session ID from favorites
     */
    fun removeFavorite(sessionId: Int) {
        val currentFavorites = getFavoriteIds().toMutableSet()
        currentFavorites.remove(sessionId)
        saveFavoriteIds(currentFavorites)
    }

    /**
     * Check if a session is favorited
     */
    fun isFavorite(sessionId: Int): Boolean {
        return getFavoriteIds().contains(sessionId)
    }

    /**
     * Toggle favorite status of a session
     * @return true if now favorited, false if unfavorited
     */
    fun toggleFavorite(sessionId: Int): Boolean {
        return if (isFavorite(sessionId)) {
            removeFavorite(sessionId)
            false
        } else {
            addFavorite(sessionId)
            true
        }
    }

    /**
     * Clear all favorites
     */
    fun clearAllFavorites() {
        sharedPreferences.edit()
            .remove(KEY_FAVORITE_IDS)
            .apply()
    }

    companion object {
        private const val PREFS_NAME = "routin_app_preferences"
        private const val KEY_FAVORITE_IDS = "favorite_session_ids"
    }
}
