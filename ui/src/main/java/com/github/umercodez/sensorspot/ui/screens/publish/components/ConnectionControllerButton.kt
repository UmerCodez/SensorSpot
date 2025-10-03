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
package com.github.umercodez.sensorspot.ui.screens.publish.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.github.umercodez.sensorspot.data.sensorpublisher.MqttConnectionState
import com.github.umercodez.sensorspot.ui.R
import com.github.umercodez.sensorspot.ui.SensorSpotTheme

@Composable
fun ConnectionControllerButton(
    modifier: Modifier = Modifier,
    mqttConnectionState: MqttConnectionState,
    onPublishClick: (() -> Unit)? = null,
    onStopClick: (() -> Unit)? = null
) {
    Button(
        modifier = modifier.aspectRatio(1f),
        onClick = {
            if(mqttConnectionState != MqttConnectionState.Connected){
                onPublishClick?.invoke()
            }

            if(mqttConnectionState == MqttConnectionState.Connected){
                onStopClick?.invoke()
            }
        },
        enabled = mqttConnectionState != MqttConnectionState.Connecting
    ) {

        when(mqttConnectionState) {
            MqttConnectionState.Connected -> {
                Icon(
                    modifier = Modifier.fillMaxSize(0.8f),
                    painter = painterResource(R.drawable.mode_off_on_24dp),
                    contentDescription = null
                )
            }
            else -> {
                Box(modifier = Modifier.fillMaxSize(0.8f)){
                    BasicText(
                        modifier = Modifier.align(Alignment.Center),
                        text = "CONNECT",
                        maxLines = 1,
                        style = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.onPrimary),
                        autoSize = TextAutoSize.StepBased(
                            minFontSize = 10.sp,
                            maxFontSize = 50.sp,
                        )

                    )
                }
            }
        }

    }

}

@Preview
@Composable
private fun MyButtonPreview() {
    SensorSpotTheme {
        ConnectionControllerButton(
            mqttConnectionState = MqttConnectionState.Disconnected,
        )
    }
}