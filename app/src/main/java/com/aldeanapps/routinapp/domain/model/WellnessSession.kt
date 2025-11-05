package com.aldeanapps.routinapp.domain.model

data class WellnessSession(
    val id: Int,
    val title: String,
    val category: String,
    val duration: Int, // in minutes
    val rating: Double,
    val imageUrl: String,
    val description: String,
    val instructor: String,
    val isFavorite: Boolean = false
)
