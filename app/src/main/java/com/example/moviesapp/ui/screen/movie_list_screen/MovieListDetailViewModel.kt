package com.example.moviesapp.ui.screen.movie_list_screen

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.R
import com.example.moviesapp.core.Result
import com.example.moviesapp.data.model.config.dao.ImageConfigurationDAO
import com.example.moviesapp.data.model.config.dao.toImageConfig
import com.example.moviesapp.domain.model.config.ImageConfig
import com.example.moviesapp.domain.model.movie.list.Movie
import com.example.moviesapp.domain.use_case.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListDetailViewModel @Inject constructor(
    private val application: Application,
    private val imageConfigurationDAO: ImageConfigurationDAO,
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {

    private val _movies = mutableStateOf<List<Movie>>(emptyList())
    val movies: State<List<Movie>> = _movies
    private val _error = mutableStateOf("")
    val error: State<String> = _error
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _imageConfig = mutableStateOf<ImageConfig?>(null)
    val imageConfig: State<ImageConfig?> = _imageConfig

    private val numberDisplayAdvertisement: Int = 3

    private var total_pages: Int = 0
    private var total_results: Int = 0

    private val page = mutableStateOf(1)

    var path: String? = null
        set(value) {
            if (field != value) {
                field = value
                getListMovies()
            }
        }

    init {
        getImageConfig()
    }

    private fun getImageConfig() {
        viewModelScope.launch {
            imageConfigurationDAO.getAllImageConfigs().first().let {
                _imageConfig.value = it.toImageConfig()
            }
        }
    }

    fun onRefresh() {
        page.value = 1
        _movies.value = emptyList()
        getListMovies()
    }

    fun getTotalRows(): Int {
        return _movies.value.size + _movies.value.size / numberDisplayAdvertisement
    }

    fun isAdvertisementCell(index: Int): Boolean {
        return index > 0 && (index + 1) % (numberDisplayAdvertisement + 1) == 0
    }

    fun getMovieByIndex(index: Int): Movie {
        val realIndex = index - (index / numberDisplayAdvertisement)
        return _movies.value[realIndex]
    }

    fun isLoadMore(index: Int): Boolean {
        return ((index + 1) >= getTotalRows()) && page.value < total_pages
    }

    fun loadMore() {
        page.value = page.value + 1
        getListMovies()
    }

    private fun getListMovies() {
        path?.let {
            getMoviesUseCase.getListMovies(it, page.value).onEach { result ->
                when (result) {
                    is Result.Success -> {
                        val newMovies = result.data?.results.orEmpty()
                        val currentMovies = ArrayList(_movies.value)
                        currentMovies.addAll(newMovies)
                        _movies.value = currentMovies.distinct()
                        _isLoading.value = false
                        _error.value = ""
                        total_pages = result.data?.total_pages ?: 1
                        total_results = result.data?.total_results ?: 0
                    }
                    is Result.Error -> {
                        _error.value =
                            result.message ?: application.getString(R.string.unknown_error)

                    }
                    is Result.Loading -> {
                        _isLoading.value = true
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}