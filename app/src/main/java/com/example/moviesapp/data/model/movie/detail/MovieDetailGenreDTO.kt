package com.example.moviesapp.data.model.movie.detail

import com.example.moviesapp.domain.model.movie.detail.MovieDetailGenre

data class MovieDetailGenreDTO(
    val id: Int,
    val name: String
)

fun MovieDetailGenreDTO.toMovieDetailGenre(): MovieDetailGenre {
    return MovieDetailGenre(
        id = id,
        name = name
    )
}


