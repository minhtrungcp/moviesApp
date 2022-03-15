package com.example.moviesapp.di

import android.content.Context
import androidx.room.Room
import com.example.moviesapp.data.model.config.dao.ImageConfigurationDAO
import com.example.moviesapp.data.model.config.dao.ImageConfigurationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalRepositoryModule {

    // local module
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ImageConfigurationDatabase {
        return Room.databaseBuilder(context, ImageConfigurationDatabase::class.java, "image_config.database")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Singleton
    @Provides
    fun cityDao(database: ImageConfigurationDatabase): ImageConfigurationDAO {
        return database.imageConfigDAO()
    }
}