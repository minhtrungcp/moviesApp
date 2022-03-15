package com.example.moviesapp.domain.repository

import com.example.moviesapp.core.Result
import com.example.moviesapp.domain.model.movie.detail.MovieDetail
import com.example.moviesapp.domain.model.movie.list.MovieList
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getListMovies(path: String, page: Int): Flow<Result<MovieList>>
    fun getMovieDetail(movie_id: String): Flow<Result<MovieDetail>>
}