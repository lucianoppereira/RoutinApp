package com.aldeanapps.routinapp.data.remote.api

import com.aldeanapps.routinapp.data.remote.dto.WellnessSessionDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit API service interface.
 * Defines endpoints for wellness session data.
 */
interface WellnessApiService {
    
    @GET("WellnessSession")
    suspend fun getSessions(): Response<List<WellnessSessionDto>>

    @GET("WellnessSession")
    suspend fun getSessionById(@Query("id") id: Int): Response<WellnessSessionDto>

    @GET("WellnessSession")
    suspend fun getSessionsByCategory(@Query("category") category: String): Response<List<WellnessSessionDto>>
}
