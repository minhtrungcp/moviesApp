package com.example.moviesapp.domain.model.movie.detail

import com.example.moviesapp.data.model.movie.detail.MovieDetailGenreDTO
import com.example.moviesapp.domain.model.config.ImageConfig
import com.example.moviesapp.domain.model.config.getBackdropSize
import com.example.moviesapp.domain.model.movie.list.Movie
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

data class MovieDetail(
    val id: Int,
    val backdrop_path: String,
    val genres: List<MovieDetailGenre>,
    val title: String,
    val overview: String,
    val release_date: String,
    val runtime: Int,
    val vote_average: Double
)

fun MovieDetail.getYearRelease(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val date = dateFormat.parse(release_date)
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.get(Calendar.YEAR).toString()
}

fun MovieDetail.getBackdropImageUrl(imageConfig: ImageConfig?): String {
    imageConfig?.let { config ->
        backdrop_path?.let { path ->
            return config.secure_base_url + config.getBackdropSize() + path
        }
    }
    return ""
}

fun MovieDetail.getRunTimeString(): String {
    val hours = TimeUnit.MINUTES.toHours(runtime.toLong())
    val remainMinutes = runtime - TimeUnit.HOURS.toMinutes(hours)
    return String.format("%dh %dmin", hours, remainMinutes)
}