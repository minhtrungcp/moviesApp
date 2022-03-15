package com.example.moviesapp.data.model.config.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageConfigurationDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertImageConfig(imageConfig : ImageConfigEntity) : Long

    @Query("select * from image_configuration_table ")
    suspend fun getAllImageConfigs(): List<ImageConfigEntity>

    @Query("delete from image_configuration_table")
    suspend fun deleteAllImageConfigs()

}