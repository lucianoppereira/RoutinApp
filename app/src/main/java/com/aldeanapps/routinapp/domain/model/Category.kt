package com.aldeanapps.routinapp.domain.model

/**
 * Enum representing different wellness session categories.
 */
enum class Category(val displayName: String) {
    YOGA("Yoga"),
    MEDITATION("Meditation"),
    MASSAGE("Massage"),
    MINDFULNESS("Mindfulness"),
    SWIMMING("Swimming"),
    DANCE("Dance"),
    FITNESS("Fitness");

    companion object {
        fun getDisplayNames(): List<String> = entries.map { it.displayName }
    }
}
