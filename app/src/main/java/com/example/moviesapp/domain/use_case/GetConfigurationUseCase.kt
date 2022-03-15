package com.example.moviesapp.domain.use_case

import com.example.moviesapp.domain.model.config.ImageConfig
import com.example.moviesapp.domain.repository.ConfigurationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetConfigurationUseCase @Inject constructor(
    private val repository: ConfigurationRepository
) {
    suspend fun getConfiguration(): Unit = repository.getConfiguration()

    fun getImageConfiguration(): Flow<ImageConfig> = repository.getImageConfiguration()
}