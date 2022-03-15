package com.example.moviesapp.domain.model.config

data class ImageConfig(
    val secure_base_url: String,
    val base_url: String,
    val backdrop_sizes: List<String>,
    val logo_sizes: List<String>,
    val poster_sizes: List<String>,
    val profile_sizes: List<String>,
    val still_sizes: List<String>
)

fun ImageConfig.getBackdropSize(size: String = "original"): String {
    return backdrop_sizes.first { it == size }
}

fun ImageConfig.getLogoSize(size: String = "original"): String {
    return logo_sizes.first { it == size }
}

fun ImageConfig.getPosterSize(size: String = "original"): String {
    return poster_sizes.first { it == size }
}