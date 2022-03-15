package com.example.moviesapp.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import com.example.moviesapp.domain.model.location.MyLocation
import com.example.moviesapp.domain.repository.LocationRepository
import com.google.android.gms.location.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val appContext: Context,
    private val client: FusedLocationProviderClient
) : LocationRepository {

    @ExperimentalCoroutinesApi
    @SuppressLint("MissingPermission")
    override fun fetchLocation(): Flow<MyLocation?> = callbackFlow {
        val locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.MINUTES.toMillis(1)
            fastestInterval = TimeUnit.MINUTES.toMillis(20)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val callBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation
                Log.e("location :", "${location.latitude} ${location.latitude}")
                val userLocation = MyLocation(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
                try {
                    this@callbackFlow.trySend(userLocation).isSuccess
                } catch (e: Exception) {
                }
            }

            override fun onLocationAvailability(p0: LocationAvailability) {
                super.onLocationAvailability(p0)
                Log.e("location available:", "")
            }
        }

        client.requestLocationUpdates(locationRequest, callBack, Looper.getMainLooper()
        ).addOnFailureListener { e ->
            close(e)
        }.addOnCompleteListener {
            client.removeLocationUpdates(callBack)
        }
        awaitClose {
            client.removeLocationUpdates(callBack)
        }
    }
}