package com.example.moviesapp.domain.use_case

import com.example.moviesapp.domain.model.location.MyLocation
import com.example.moviesapp.domain.repository.LocationRepository

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocationUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    fun fetchLocation(): Flow<MyLocation?> =
        repository.fetchLocation()
}