package com.example.moviesapp.ui.navigation

sealed class ScreenName(val route: String){
    object MapScreen: ScreenName("map_screen")
    object MovieListScreen: ScreenName("movie_list_screen")
    object MovieDetailScreen: ScreenName("movie_detail_screen")
}