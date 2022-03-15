package com.example.moviesapp.data.model.movie.detail

import com.example.moviesapp.domain.model.movie.detail.MovieDetail

data class MovieDetailDTO(
    val id: Int,
    val backdrop_path: String,
    val genres: List<MovieDetailGenreDTO>,
    val title: String,
    val overview: String,
    val release_date: String,
    val runtime: Int,
    val vote_average: Double
)

fun MovieDetailDTO.toMovieDetail(): MovieDetail {
    return MovieDetail(
        id = id,
        backdrop_path = backdrop_path,
        genres = genres.map { it.toMovieDetailGenre() },
        title = title,
        overview = overview,
        release_date = release_date,
        runtime = runtime,
        vote_average = vote_average
    )
}


