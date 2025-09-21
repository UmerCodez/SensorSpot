/*
 *     This file is a part of SensorSpot (https://www.github.com/UmerCodez/SensorSpot)
 *     Copyright (C) 2025 Umer Farooq (umerfarooq2383@gmail.com)
 *
 *     SensorSpot is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SensorSpot is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SensorSpot. If not, see <https://www.gnu.org/licenses/>.
 *
 */
package com.github.umercodez.sensorspot.ui.screens.sensors

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.umercodez.sensorspot.data.repositories.settings.sensor.SensorsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SensorScreenViewModel @Inject constructor(
    private val sensorsRepository: SensorsRepository
): ViewModel(){

    private val _uiState = MutableStateFlow(SensorsScreenState())
    val uiState = _uiState.asStateFlow()
    val tag = "SensorScreenViewModel"

    init {

        Log.d(tag, "init() ${hashCode()}")


        viewModelScope.launch{
            _uiState.value.allSensors.clear()
            _uiState.value.allSensors.addAll(sensorsRepository.getAvailableSensors())

            sensorsRepository.getSelectedSensors().forEach { sensor ->
                _uiState.value.sensorSelectionState[sensor] = true
            }

        }

        viewModelScope.launch {
            sensorsRepository.getSelectedSensorsAsFlow().collect { selectedSensors ->
                _uiState.update {
                    it.copy(selectedSensorsCount = selectedSensors.size)
                }
            }
        }

    }

    fun onEvent(event: SensorsScreenEvent) {
        when (event) {
            is SensorsScreenEvent.OnSensorItemCheckedChange -> {
                if (event.checked) {
                    _uiState.value.sensorSelectionState[event.sensor] = true
                } else {
                    _uiState.value.sensorSelectionState[event.sensor] = false
                }

                viewModelScope.launch {
                    val selectedSensors = _uiState.value.sensorSelectionState.filterValues { it == true }.keys.toList()
                    sensorsRepository.saveSelectedSensors(selectedSensors)
                }
            }
        }
    }

}