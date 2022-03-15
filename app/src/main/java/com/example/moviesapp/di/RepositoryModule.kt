package com.example.moviesapp.di

import android.app.Application
import android.content.Context
import com.example.moviesapp.data.MovieApiService
import com.example.moviesapp.data.model.config.dao.ImageConfigurationDAO
import com.example.moviesapp.data.repository.ConfigurationRepositoryImpl
import com.example.moviesapp.data.repository.LocationRepositoryImpl
import com.example.moviesapp.data.repository.MovieRepositoryImpl
import com.example.moviesapp.domain.repository.ConfigurationRepository
import com.example.moviesapp.domain.repository.LocationRepository
import com.example.moviesapp.domain.repository.MovieRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideLocationProviderClient(application: Application): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(application.applicationContext)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
        @ApplicationContext appContext: Context,
        client: FusedLocationProviderClient
    ): LocationRepository = LocationRepositoryImpl(appContext, client)

    @Provides
    @Singleton
    fun provideMovieRepository(
        @ApplicationContext appContext: Context,
        movieApiService: MovieApiService
    ): MovieRepository = MovieRepositoryImpl(appContext, movieApiService)

    @Provides
    @Singleton
    fun provideConfigurationRepository(
        movieApiService: MovieApiService,
        imageConfigurationDAO: ImageConfigurationDAO
    ): ConfigurationRepository = ConfigurationRepositoryImpl(movieApiService, imageConfigurationDAO)
}