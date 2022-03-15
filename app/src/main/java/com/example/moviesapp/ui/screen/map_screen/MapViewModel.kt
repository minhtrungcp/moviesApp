package com.example.moviesapp.ui.screen.map_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.domain.model.location.MyLocation
import com.example.moviesapp.domain.use_case.GetConfigurationUseCase
import com.example.moviesapp.domain.use_case.GetLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getLocationUseCase: GetLocationUseCase,
    private val getConfigurationUseCase: GetConfigurationUseCase
) : ViewModel() {
    private val _performLocationAction = mutableStateOf(false)
    val performLocationAction: State<Boolean> = _performLocationAction

    private val _currentLocation = mutableStateOf<MyLocation?>(null)
    val currentLocation = _currentLocation

    fun setPerformLocationAction(request: Boolean) {
        _performLocationAction.value = request
        if (request) {
            getCurrentLocation()
        }
    }

    init {
        getConfiguration()
    }


    private fun getConfiguration() {
        viewModelScope.launch {
            getConfigurationUseCase.getConfiguration()
        }
    }

    fun getCurrentLocation() {
        getLocationUseCase.fetchLocation().onEach { myLocation ->
            myLocation?.let {
                _currentLocation.value = it
            }
        }.launchIn(viewModelScope)
    }
}