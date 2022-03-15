package com.example.moviesapp.data

import com.example.moviesapp.BuildConfig
import com.example.moviesapp.data.model.config.dto.ConfigurationDTO
import com.example.moviesapp.data.model.movie.detail.MovieDetailDTO
import com.example.moviesapp.data.model.movie.list.MovieResponseDTO

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {
    @GET("movie/{path}")
    suspend fun getListMovies(
        @Path("path") path: String,
        @Query("page") page: Int = 1,
        @Query("api_key") api_key: String = BuildConfig.API_KEY,
    ): MovieResponseDTO

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movie_id: String,
        @Query("api_key") api_key: String = BuildConfig.API_KEY
    ): MovieDetailDTO

    @GET("configuration")
    suspend fun getConfiguration(
        @Query("api_key") api_key: String = BuildConfig.API_KEY
    ): ConfigurationDTO
}