package com.example.moviesapp.data.model.config.dao

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ImageConfigEntity::class], version = 1, exportSchema = false)
abstract class ImageConfigurationDatabase : RoomDatabase() {

    abstract fun imageConfigDAO(): ImageConfigurationDAO

}
