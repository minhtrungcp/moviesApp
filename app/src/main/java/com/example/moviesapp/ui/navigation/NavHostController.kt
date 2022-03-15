package com.example.moviesapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moviesapp.ui.screen.map_screen.MapScreen
import com.example.moviesapp.ui.screen.movie_detail_screen.MovieDetailScreen
import com.example.moviesapp.ui.screen.movie_list_screen.MovieListMainScreen

@Composable
fun NavHostController(
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ScreenName.MapScreen.route
    ) {
        composable(
            route = ScreenName.MapScreen.route
        ) {
            MapScreen(navController)
        }
        composable(
            route = ScreenName.MovieListScreen.route
        ) {
            MovieListMainScreen(navController)
        }
        composable(
            route = ScreenName.MovieDetailScreen.route + "/{movie_id}"
        ) {
            MovieDetailScreen(navController)
        }
    }
}