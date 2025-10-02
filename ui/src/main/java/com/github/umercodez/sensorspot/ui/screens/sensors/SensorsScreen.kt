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

import android.Manifest
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.umercodez.sensorspot.data.repositories.settings.sensor.fakeSensors
import com.github.umercodez.sensorspot.ui.SensorSpotTheme
import com.github.umercodez.sensorspot.ui.screens.sensors.components.GpsItem
import com.github.umercodez.sensorspot.ui.screens.sensors.components.SensorItem
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SensorsScreen(
    viewModel: SensorScreenViewModel = hiltViewModel(),
    onSelectedSensorsCountChange: ((Int) -> Unit)? = null
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(state.selectedSensorsCount) {
        onSelectedSensorsCountChange?.invoke(state.selectedSensorsCount)
    }

    LaunchedEffect(locationPermissionState.status) {
        viewModel.onLocationPermissionStateChange()
    }

    SensorsScreen(
        state = state,
        onEvent = { event ->

            if(event is SensorsScreenEvent.OnGrantLocationPermissionClick) {
                locationPermissionState.launchPermissionRequest()
            }

            viewModel.onEvent(event)
        }
    )

}


@Composable
fun SensorsScreen(
    state: SensorsScreenState,
    onEvent: (SensorsScreenEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
    ) {
        items(
            items = state.allSensors,
            key = { it.hashCode() }
        ) { sensor ->
            SensorItem(
                modifier = Modifier.animateItem(),
                sensor = sensor,
                checked = state.sensorSelectionState[sensor] == true,
                onCheckedChange = {
                    onEvent(SensorsScreenEvent.OnSensorItemCheckedChange(sensor, it))
                }
            )
        }
        item {
            GpsItem(
                checked = state.gpsChecked,
                onCheckedChange = {
                    onEvent(SensorsScreenEvent.OnGpsItemCheckedChange(it))
                },
                locationPermissionGranted = state.locationPermissionGranted,
                onGrantLocationPermissionClick = {
                    onEvent(SensorsScreenEvent.OnGrantLocationPermissionClick)
                }
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
fun SensorsScreenContentPreview() {
    SensorSpotTheme {
        SensorsScreen(
            state = SensorsScreenState(
                allSensors = fakeSensors.toMutableStateList(),
            ),
            onEvent = {}
        )
    }
}

