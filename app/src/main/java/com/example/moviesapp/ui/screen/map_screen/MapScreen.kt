package com.example.moviesapp.ui.screen.map_screen

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.example.moviesapp.R
import com.example.moviesapp.domain.model.location.MyLocation
import com.example.moviesapp.ui.navigation.ScreenName
import com.example.moviesapp.ui.permission.PermissionAction
import com.example.moviesapp.ui.permission.PermissionUI
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions
import com.google.android.libraries.maps.model.PolylineOptions
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@Composable
fun MapScreen(
    navController: NavHostController,
    viewModel: MapViewModel = hiltViewModel()
) {
    val performLocationAction = viewModel.performLocationAction.value
    val context = LocalContext.current
    val mapView = rememberMapViewWithLifecycle()
    Scaffold(
        topBar = { TopBar() }
    ) {
        if (!performLocationAction) {
            val errorMessage = stringResource(R.string.location_permission_error)
            val settingResultRequest = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult()
            ) { activityResult ->
                if (activityResult.resultCode == RESULT_OK)
                    viewModel.getCurrentLocation()
                else {
                    Toast.makeText(
                        context,
                        errorMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            checkPermission(context, errorMessage, viewModel, settingResultRequest)
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color.White),
        ) {
            AndroidView({ mapView }) { mapView ->
                CoroutineScope(Dispatchers.Main).launch {
                    val map = mapView.awaitMap()
                    map.uiSettings.isZoomControlsEnabled = true
                    var currentnLocation = viewModel.currentLocation.value
                    currentnLocation?.let {
                        map.isMyLocationEnabled = true
                        map.uiSettings?.isMyLocationButtonEnabled = true
                        val mark = LatLng(it.latitude, it.longitude)
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, 12f))
                    }
                    var newLocation: MyLocation? = null
                    mapView.getMapAsync {
                        it.mapType = 1
                        it.uiSettings.isZoomControlsEnabled = true
                        it.setOnCameraIdleListener {
                            it.clear()
                            it.addMarker(
                                MarkerOptions()
                                    .position(it.cameraPosition.target)
                            )
                            newLocation = MyLocation(
                                it.cameraPosition.target.latitude,
                                it.cameraPosition.target.longitude
                            )
                        }
                        it.setOnMarkerClickListener {
                            currentnLocation?.let { first ->
                                newLocation?.let { second ->
                                    map.addPolyline(
                                        PolylineOptions().add(
                                            LatLng(
                                                first.latitude,
                                                first.longitude
                                            ),
                                            LatLng(second.latitude, second.longitude)
                                        )
                                    )
                                }
                            }

                            navController.navigate(ScreenName.MovieListScreen.route)
                            true
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
        }
    }

    // Makes MapView follow the lifecycle of this composable
    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

@Composable
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException()
            }
        }
    }

@Composable
fun TopBar() {
    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.map_screen),
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
    )
}

@Composable
fun checkPermission(
    context: Context,
    errorMessage: String,
    viewModel: MapViewModel,
    settingResultRequest: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>
) {
    PermissionUI(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) { permissionAction ->
        when (permissionAction) {
            is PermissionAction.OnPermissionGranted -> {
                checkGPSEnable(
                    context = context,
                    onDisabled = { intentSenderRequest ->
                        settingResultRequest.launch(intentSenderRequest)
                    },
                    onEnabled = { viewModel.setPerformLocationAction(true) }
                )
            }
            is PermissionAction.OnPermissionDenied -> {
                viewModel.setPerformLocationAction(false)
                Toast.makeText(
                    context,
                    errorMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}

fun checkGPSEnable(
    context: Context,
    onDisabled: (IntentSenderRequest) -> Unit,
    onEnabled: () -> Unit
) {

    val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
        interval = TimeUnit.SECONDS.toMillis(100)
        fastestInterval = TimeUnit.SECONDS.toMillis(20)
        priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    val client: SettingsClient = LocationServices.getSettingsClient(context)
    val builder: LocationSettingsRequest.Builder = LocationSettingsRequest
        .Builder()
        .addLocationRequest(locationRequest)

    val gpsSettingTask: Task<LocationSettingsResponse> =
        client.checkLocationSettings(builder.build())

    gpsSettingTask.addOnSuccessListener { onEnabled() }
    gpsSettingTask.addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
            try {
                val intentSenderRequest = IntentSenderRequest
                    .Builder(exception.resolution)
                    .build()
                onDisabled(intentSenderRequest)
            } catch (sendEx: IntentSender.SendIntentException) {
                // ignore here
            }
        }
    }
}
