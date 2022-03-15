package com.example.moviesapp.ui.screen.movie_list_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moviesapp.R
import com.example.moviesapp.ui.common.DetailTopBar
import com.example.moviesapp.ui.screen.movie_list_screen.model.MovieListType


@Composable
fun MovieListMainScreen(
    navController: NavHostController
) {
    val bottomBarNav = rememberNavController()

    Scaffold(
        topBar = { DetailTopBar(stringResource(R.string.movie_list), navController) },
        bottomBar = { BottomNavigationBar(bottomBarNav) },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            MovieListDetailNav(navController = bottomBarNav, navController)
        }
    }
}

@Composable
fun MovieListDetailNav(navController: NavHostController, mainNav: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = MovieListType.NowPlaying.route
    ) {
        composable(
            route = MovieListType.NowPlaying.route
        ) {
            MovieListDetailScreen(
                movieListPath = MovieListType.NowPlaying.route,
                navController = mainNav
            )
        }
        composable(
            route = MovieListType.TopRated.route
        ) {
            MovieListDetailScreen(
                movieListPath = MovieListType.TopRated.route,
                navController = mainNav
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        MovieListType.NowPlaying,
        MovieListType.TopRated
    )
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                selected = item.route == currentRoute,
                icon = {
                    val icon =
                        if (item.route == MovieListType.TopRated.route) R.drawable.ic_baseline_star_rate_24 else R.drawable.ic_baseline_play_arrow_24
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null
                    )
                },
                label = {
                    val title =
                        if (item.route == MovieListType.TopRated.route) stringResource(R.string.movie_top_rating) else stringResource(
                            R.string.movie_playing
                        )
                    Text(text = title)
                },
                alwaysShowLabel = true,
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(0.4f),
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}