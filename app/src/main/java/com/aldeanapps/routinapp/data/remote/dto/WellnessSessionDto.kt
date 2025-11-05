package com.aldeanapps.routinapp.data.remote.dto

import com.aldeanapps.routinapp.domain.model.WellnessSession
import com.google.gson.annotations.SerializedName

data class WellnessSessionDto(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("category")
    val category: String,
    
    @SerializedName("duration")
    val duration: Int,
    
    @SerializedName("rating")
    val rating: Double,
    
    @SerializedName("image")
    val imageUrl: String,
    
    @SerializedName("description")
    val description: String,
    
    @SerializedName("instructor")
    val instructor: String,
)

fun WellnessSessionDto.toDomain(isFavorite: Boolean = false) =
    WellnessSession(
        id = id,
        title = title,
        category = category,
        duration = duration,
        rating = rating,
        imageUrl = imageUrl,
        description = description,
        instructor = instructor,
        isFavorite = isFavorite,
    )
