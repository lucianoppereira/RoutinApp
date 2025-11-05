package com.aldeanapps.routinapp.domain.usecase

import com.aldeanapps.routinapp.data.local.preferences.PreferencesManager
import javax.inject.Inject

/**
 * Use case for getting favorite session IDs from local storage.
 */
class GetFavoriteIdsUseCase @Inject constructor(
    private val preferencesManager: PreferencesManager
) {
    operator fun invoke(): Set<Int> {
        return preferencesManager.getFavoriteIds()
    }
}
