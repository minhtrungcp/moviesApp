package com.example.moviesapp.data.model.movie.list

import com.example.moviesapp.domain.model.movie.list.MovieList


data class MovieResponseDTO(
    val page: Int,
    val results: List<MovieDTO>,
    val total_pages: Int,
    val total_results: Int
)

fun MovieResponseDTO.toMovieList() = MovieList(
    page = page,
    results = results.map { it.toMovie() },
    total_pages = total_pages,
    total_results = total_results
)