package com.example.moviesapp.ui.screen.movie_list_screen.model

sealed class MovieListType(
    var route: String,
    var title: String,
) {
    object TopRated : MovieListType("top_rated", "Top Rated")
    object NowPlaying : MovieListType("now_playing", "Now Playing")
}
