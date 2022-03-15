package com.example.moviesapp.domain.use_case

import com.example.moviesapp.core.Result
import com.example.moviesapp.domain.model.movie.detail.MovieDetail
import com.example.moviesapp.domain.model.movie.list.MovieList
import com.example.moviesapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    fun getListMovies(path: String, page: Int): Flow<Result<MovieList>> =
        repository.getListMovies(path, page)

    fun getMovieDetail(movie_id: String): Flow<Result<MovieDetail>> =
        repository.getMovieDetail(movie_id)
}