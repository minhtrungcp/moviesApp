package com.example.moviesapp.domain.model.movie.list

data class MovieList(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)
