package com.example.moviesapp.ui.screen.movie_detail_screen

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.R
import com.example.moviesapp.core.Result
import com.example.moviesapp.data.model.config.dao.ImageConfigurationDAO
import com.example.moviesapp.data.model.config.dao.toImageConfig
import com.example.moviesapp.domain.model.config.ImageConfig
import com.example.moviesapp.domain.model.movie.detail.MovieDetail

import com.example.moviesapp.domain.use_case.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val imageConfigurationDAO: ImageConfigurationDAO,
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {
    private val _movieDetail = mutableStateOf<MovieDetail?>(null)
    val movieDetail: State<MovieDetail?> = _movieDetail
    private val _error = mutableStateOf("")
    val error: State<String> = _error
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _imageConfig = mutableStateOf<ImageConfig?>(null)
    val imageConfig: State<ImageConfig?> = _imageConfig

    init {
        getImageConfig()
    }

    private fun getImageConfig() {
        viewModelScope.launch {
            val imageConfigEntity = imageConfigurationDAO.getAllImageConfigs().first()
            _imageConfig.value = imageConfigEntity.toImageConfig()
            getMovieDetail()
        }
    }


    fun getMovieDetail() {
        savedStateHandle.get<String>("movie_id")?.let { movie_id ->
            getMoviesUseCase.getMovieDetail(movie_id).onEach { result ->
                when (result) {
                    is Result.Success -> {
                        _movieDetail.value = result.data
                    }
                    is Result.Error -> {
                        _error.value = result.message ?: application.getString(R.string.unknown_error)
                        _movieDetail.value = null
                    }
                    is Result.Loading -> {
                        _isLoading.value = true
                        _movieDetail.value = null
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}