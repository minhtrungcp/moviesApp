package com.example.moviesapp.ui.screen.movie_detail_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.moviesapp.R
import com.example.moviesapp.domain.model.movie.detail.getBackdropImageUrl
import com.example.moviesapp.domain.model.movie.detail.getRunTimeString
import com.example.moviesapp.domain.model.movie.detail.getYearRelease
import com.example.moviesapp.ui.common.DetailTopBar
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun MovieDetailScreen(
    navController: NavController,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    val movieDetail = viewModel.movieDetail.value
    val imageConfig = viewModel.imageConfig.value
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    Scaffold(
        topBar = { DetailTopBar(movieDetail?.title ?: "", navController) },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = {
                        viewModel.getMovieDetail()
                    },
                ) {
                    Column {
                        movieDetail?.let {
                            val painter =
                                rememberImagePainter(data = it.getBackdropImageUrl(
                                    imageConfig
                                ),
                                    builder = {
                                        placeholder(R.drawable.ic_baseline_image_24)
                                    })
                            Image(
                                painter = painter,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .size(200.dp)
                            )
                            Divider(color = Color.Gray, thickness = 1.dp)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 15.dp,
                                        end = 15.dp,
                                        top = 5.dp,
                                        bottom = 5.dp
                                    )
                            ) {
                                Column() {
                                    Text(
                                        text = "${it.title} (${it.getYearRelease()})",
                                        style = MaterialTheme.typography.h5
                                    )
                                    Row() {
                                        Image(
                                            modifier = Modifier
                                                .size(17.dp)
                                                .align(Alignment.CenterVertically),
                                            painter = painterResource(id = com.example.moviesapp.R.drawable.ic_baseline_star_rate_24),
                                            contentDescription = null,
                                        )

                                        Text(
                                            text = "${it.vote_average}/10 |",
                                            style = MaterialTheme.typography.body2,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        Text(
                                            text = it.getRunTimeString(),
                                            style = MaterialTheme.typography.body2,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.padding(horizontal = 5.dp)
                                        )

                                        var genres = "| "
                                        it.genres.forEach { genre ->
                                            genres += genre.name
                                            if (genre.id != it.genres.last().id)
                                                genres += ", "
                                        }

                                        Text(
                                            text = genres,
                                            style = MaterialTheme.typography.body2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                            Divider(color = Color.Gray, thickness = 1.dp)
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 15.dp,
                                        end = 15.dp,
                                        top = 5.dp,
                                        bottom = 5.dp
                                    ),
                                text = it.overview,
                                style = MaterialTheme.typography.body2,
                            )
                            Divider(color = Color.LightGray, thickness = 15.dp)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 15.dp,
                                        end = 15.dp,
                                        top = 5.dp,
                                        bottom = 5.dp
                                    )
                            ) {
                                Text(
                                    text = stringResource(R.string.cast_and_crews),
                                    style = MaterialTheme.typography.h5,
                                )
                            }
                        }
                    }
                }
                if (viewModel.error.value.isNotEmpty()) {
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
                                viewModel.getMovieDetail()
                            },
                            colors = ButtonDefaults.textButtonColors(
                                backgroundColor = Color.Gray
                            )
                        ) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }
                if (viewModel.isLoading.value && movieDetail == null) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    )
}