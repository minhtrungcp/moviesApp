package com.example.moviesapp.data.repository

import android.util.Log
import androidx.annotation.WorkerThread
import com.example.moviesapp.data.MovieApiService
import com.example.moviesapp.data.model.config.dao.ImageConfigurationDAO
import com.example.moviesapp.data.model.config.dao.toImageConfig
import com.example.moviesapp.data.model.config.dto.toImageConfigEntity
import com.example.moviesapp.domain.model.config.ImageConfig
import com.example.moviesapp.domain.repository.ConfigurationRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ConfigurationRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService,
    private val imageConfigurationDAO: ImageConfigurationDAO
) : ConfigurationRepository {

    @WorkerThread
    override suspend fun getConfiguration(): Unit = withContext(IO) {
        try {
            val configuration = apiService.getConfiguration()
            configuration.images?.let {
                imageConfigurationDAO.deleteAllImageConfigs()
                imageConfigurationDAO.insertImageConfig(it.toImageConfigEntity())
            }
        } catch (e: HttpException) {
            Log.e("Get config error ", e.message.orEmpty())
        } catch (e: IOException) {
            Log.e("Database error ", e.message.orEmpty())
        }
    }

    override fun getImageConfiguration(): Flow<ImageConfig> = flow {
        try {
            val response = imageConfigurationDAO.getAllImageConfigs()
            emit(response.first().toImageConfig())
        } catch (e: IOException) {
            Log.e("Database error ", e.message.orEmpty())
        }
    }
}