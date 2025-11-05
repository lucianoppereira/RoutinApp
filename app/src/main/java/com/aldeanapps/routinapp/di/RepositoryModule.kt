package com.aldeanapps.routinapp.di

import com.aldeanapps.routinapp.data.repository.WellnessRepositoryImpl
import com.aldeanapps.routinapp.domain.repository.WellnessRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for repository bindings.
 * Uses @Binds for more efficient interface-to-implementation binding.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindWellnessRepository(
        wellnessRepositoryImpl: WellnessRepositoryImpl
    ): WellnessRepository
}
