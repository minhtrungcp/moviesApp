package com.example.moviesapp.ui.screen.movie_list_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.moviesapp.R
import com.example.moviesapp.ui.navigation.ScreenName
import com.example.moviesapp.ui.screen.movie_list_screen.cell.MovieAdvertiseCell
import com.example.moviesapp.ui.screen.movie_list_screen.cell.MovieItemCell
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun MovieListDetailScreen(
    movieListPath: String,
    navController: NavController,
    viewModel: MovieListDetailViewModel = hiltViewModel()
) {
    viewModel.path = movieListPath
    val state = viewModel.movies.value
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.onRefresh()
            },
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(viewModel.getTotalRows()) { index ->
                    if (viewModel.isLoadMore(index)) {
                        viewModel.loadMore()
                    }
                    if (viewModel.isAdvertisementCell(index))
                        MovieAdvertiseCell()
                    else {
                        val movie = viewModel.getMovieByIndex(index)
                        MovieItemCell(
                            imageConfig = viewModel.imageConfig.value,
                            movie = movie,
                            onItemClick = {
                                navController.navigate(ScreenName.MovieDetailScreen.route + "/${movie.id}")
                            })
                    }
                }
            }
            if (viewModel.error.value.isNotBlank()) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = viewModel.error.value,
                        color = MaterialTheme.colors.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            viewModel.onRefresh()
                        },
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = Color.Gray
                        )
                    ) {
                        Text(stringResource(R.string.retry))
                    }
                }
            } else
                if (viewModel.isLoading.value && state.isEmpty()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
        }
    }
}