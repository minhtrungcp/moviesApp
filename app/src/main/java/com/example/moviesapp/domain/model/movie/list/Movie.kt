package com.example.moviesapp.domain.model.movie.list

import com.example.moviesapp.domain.model.config.ImageConfig
import com.example.moviesapp.domain.model.config.getPosterSize
import java.text.SimpleDateFormat
import java.util.*

data class Movie(
    val id: Int,
    val poster_path: String?,
    val backdrop_path: String?,
    val title: String,
    val vote_average: Double,
    val vote_count: Int,
    val release_date: String,
    val overview: String,
)

fun Movie.getYearRelease(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val date = dateFormat.parse(release_date)
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.get(Calendar.YEAR).toString()
}

fun Movie.getMovieImageUrl(imageConfig: ImageConfig?, path: String?): String {
    imageConfig?.let { config ->
        path?.let { path ->
            return config.secure_base_url + config.getPosterSize() + path
        }
    }
    return ""
}
