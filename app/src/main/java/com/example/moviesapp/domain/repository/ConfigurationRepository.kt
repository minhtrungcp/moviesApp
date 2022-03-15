package com.example.moviesapp.domain.repository

import com.example.moviesapp.domain.model.config.ImageConfig
import kotlinx.coroutines.flow.Flow

interface ConfigurationRepository {
    suspend fun getConfiguration(): Unit

    fun getImageConfiguration(): Flow<ImageConfig>
}