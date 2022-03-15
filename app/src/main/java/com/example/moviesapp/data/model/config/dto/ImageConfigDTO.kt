package com.example.moviesapp.data.model.config.dto

import com.example.moviesapp.data.model.config.dao.ImageConfigEntity

data class ImageConfigDTO(
    val secure_base_url: String,
    val base_url: String,
    val backdrop_sizes: List<String>,
    val logo_sizes: List<String>,
    val poster_sizes: List<String>,
    val profile_sizes: List<String>,
    val still_sizes: List<String>
)

fun ImageConfigDTO.toImageConfigEntity(): ImageConfigEntity {
    return ImageConfigEntity(
        secure_base_url = secure_base_url,
        base_url = base_url,
        backdrop_sizes = backdrop_sizes,
        logo_sizes = logo_sizes,
        poster_sizes = poster_sizes,
        profile_sizes = profile_sizes,
        still_sizes = still_sizes,
    )
}
