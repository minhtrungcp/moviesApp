package com.example.moviesapp.data.model.config.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.moviesapp.core.Converters
import com.example.moviesapp.domain.model.config.ImageConfig

@Entity(tableName = "image_configuration_table")
@TypeConverters(Converters::class)
data class ImageConfigEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var secure_base_url: String,
    var base_url: String,
    var backdrop_sizes: List<String>,
    var logo_sizes: List<String>,
    var poster_sizes: List<String>,
    var profile_sizes: List<String>,
    var still_sizes: List<String>
)

fun ImageConfigEntity.toImageConfig(): ImageConfig {
    return ImageConfig(
        secure_base_url= secure_base_url,
        base_url= base_url,
        backdrop_sizes= backdrop_sizes,
        logo_sizes= logo_sizes,
        poster_sizes= poster_sizes,
        profile_sizes= profile_sizes,
        still_sizes= still_sizes,
    )
}
