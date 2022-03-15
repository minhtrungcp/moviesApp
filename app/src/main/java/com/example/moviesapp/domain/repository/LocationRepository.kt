package com.example.moviesapp.domain.repository

import com.example.moviesapp.domain.model.location.MyLocation
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun fetchLocation(): Flow<MyLocation?>
}