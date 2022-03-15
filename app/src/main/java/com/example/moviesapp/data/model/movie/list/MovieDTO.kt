package com.example.moviesapp.data.model.movie.list

import com.example.moviesapp.domain.model.movie.list.Movie

data class MovieDTO(
    val id: Int,
    val poster_path: String?,
    val backdrop_path: String?,
    val title: String,
    val vote_average: Double,
    val vote_count: Int,
    val release_date: String,
    val overview: String,
)

fun MovieDTO.toMovie(): Movie {
    return Movie(
        id = id,
        poster_path = poster_path,
        backdrop_path = backdrop_path,
        title = title,
        vote_average = vote_average,
        vote_count = vote_count,
        release_date = release_date,
        overview = overview
    )
}
