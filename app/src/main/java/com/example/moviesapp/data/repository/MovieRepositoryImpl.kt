package com.example.moviesapp.data.repository

import android.content.Context
import com.example.moviesapp.R
import com.example.moviesapp.core.Result
import com.example.moviesapp.data.MovieApiService
import com.example.moviesapp.data.model.movie.detail.toMovieDetail
import com.example.moviesapp.data.model.movie.list.toMovieList
import com.example.moviesapp.domain.model.movie.detail.MovieDetail
import com.example.moviesapp.domain.model.movie.list.MovieList
import com.example.moviesapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val appContext: Context,
    private val apiService: MovieApiService
) : MovieRepository {

    override fun getListMovies(path: String, page: Int): Flow<Result<MovieList>> = flow {
        try {
            emit(Result.Loading<MovieList>())
            val response = apiService.getListMovies(path = path, page)
            emit(Result.Success<MovieList>(response.toMovieList()))
        } catch (e: HttpException) {
            emit(
                Result.Error<MovieList>(
                    e.localizedMessage ?: appContext.getString(R.string.unknown_error)
                )
            )
        } catch (e: IOException) {
            emit(Result.Error<MovieList>(appContext.getString(R.string.network_error)))
        }
    }

    override fun getMovieDetail(movie_id: String) = flow {
        try {
            emit(Result.Loading<MovieDetail>())
            val response = apiService.getMovieDetail(movie_id)
            emit(Result.Success(response.toMovieDetail()))
        } catch (e: HttpException) {
            emit(
                Result.Error<MovieDetail>(
                    e.localizedMessage ?: appContext.getString(R.string.unknown_error)
                )
            )
        } catch (e: IOException) {
            emit(Result.Error<MovieDetail>(appContext.getString(R.string.network_error)))
        }
    }
}