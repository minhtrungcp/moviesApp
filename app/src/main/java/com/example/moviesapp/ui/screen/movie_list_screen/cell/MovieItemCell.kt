package com.example.moviesapp.ui.screen.movie_list_screen.cell

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.moviesapp.R
import com.example.moviesapp.domain.model.config.ImageConfig
import com.example.moviesapp.domain.model.movie.list.Movie
import com.example.moviesapp.domain.model.movie.list.getMovieImageUrl
import com.example.moviesapp.domain.model.movie.list.getYearRelease

@Composable
fun MovieItemCell(
    imageConfig: ImageConfig?,
    movie: Movie,
    onItemClick: (Movie) -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(
                start = 10.dp,
                top = 5.dp,
                bottom = 5.dp,
                end = 10.dp
            )
            .fillMaxWidth()
            .clickable { onItemClick(movie) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            imageConfig?.let {
                val painter =
                    rememberImagePainter(data = movie.getMovieImageUrl(imageConfig, movie.poster_path),
                        builder = {
                            placeholder(R.drawable.ic_baseline_image_24)
                        })

                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.size(128.dp)
                )
            }

            Column(
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.h6,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
                Text(
                    text = movie.getYearRelease(),
                    style = MaterialTheme.typography.body2,
                )
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3,
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                )
                Row (){
                    Image(
                        modifier = Modifier
                            .size(17.dp)
                            .align(Alignment.CenterVertically),
                        painter = painterResource(id = com.example.moviesapp.R.drawable.ic_baseline_star_rate_24),
                        contentDescription = null,
                    )

                    Text(
                        text = movie.vote_average.toString(),
                        style = MaterialTheme.typography.body2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}