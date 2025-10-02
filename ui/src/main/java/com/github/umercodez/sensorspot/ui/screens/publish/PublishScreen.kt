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
package com.github.umercodez.sensorspot.ui.screens.publish

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.umercodez.sensorspot.data.sensorpublisher.MqttConfig
import com.github.umercodez.sensorspot.data.sensorpublisher.MqttConnectionState
import com.github.umercodez.sensorspot.ui.SensorSpotTheme
import com.github.umercodez.sensorspot.ui.screens.publish.components.ConnectionControllerButton
import com.github.umercodez.sensorspot.ui.screens.publish.components.ConnectionStatusCard
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PublishScreen(
    viewModel: PublishScreenViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

   if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
       val notificationPermissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
       LaunchedEffect(Unit) {
           notificationPermissionState.launchPermissionRequest()
       }
   }

    PublishScreen(
        state = state,
        onEvent = viewModel::onEvent
    )

}

@Composable
fun PublishScreen(
    state: PublishScreenState,
    onEvent: (PublishScreenEvent) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //val wideScreen = maxWidth > maxHeight
        ConnectionStatusCard(
            modifier = Modifier
                .padding(bottom = 100.dp)
                .fillMaxWidth(0.7f)
            // Aligns the CARD itself to the top-center of the BoxWithConstraints
            ,
            mqttConnectionState = state.mqttConnectionState,
            time = state.elapsedTime
        )

        ConnectionControllerButton(
            modifier = Modifier
                .size(150.dp),
            mqttConnectionState = state.mqttConnectionState,
            onPublishClick = {
                onEvent(PublishScreenEvent.OnPublishClick)
            },
            onStopClick = {
                onEvent(PublishScreenEvent.OnStopClick)
            }
        )

    }

}


@Preview(showBackground = true)
@Composable
fun PublishScreenPreview() {
    SensorSpotTheme {
        Surface {
            PublishScreen(
                state = PublishScreenState(
                    mqttConfig = MqttConfig(),
                    mqttConnectionState = MqttConnectionState.CONNECTED
                ),
                onEvent = {}
            )
        }
    }
}
