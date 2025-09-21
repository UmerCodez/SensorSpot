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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.umercodez.sensorspot.data.clock.ElapsedTime
import com.github.umercodez.sensorspot.data.sensorpublisher.MqttConnectionState
import com.github.umercodez.sensorspot.ui.SensorSpotTheme


@Composable
fun ConnectionStatusCard(
    modifier: Modifier = Modifier,
    mqttConnectionState: MqttConnectionState,
    time: ElapsedTime
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                modifier = Modifier
                    .weight(0.2f)
                    .fillMaxSize()
                    .padding(10.dp),
                imageVector = when(mqttConnectionState){
                    MqttConnectionState.CONNECTING -> Icons.Filled.Info
                    MqttConnectionState.CONNECTED -> Icons.Filled.CheckCircle
                    MqttConnectionState.DISCONNECTED -> Icons.Filled.Info
                    MqttConnectionState.CONNECTION_ERROR -> Icons.Filled.Warning
                    MqttConnectionState.CONNECTION_TIMEOUT -> Icons.Filled.Warning
                },
                contentDescription = null
            )

            Column(
                modifier = Modifier.weight(0.6f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Status",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                BasicText(
                    text = mqttConnectionState.name.toString().replace("_", " "),
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.padding(horizontal = 10.dp),
                    maxLines = 1,
                    autoSize = TextAutoSize.StepBased(
                        maxFontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    )
                )
            }

            Box(
                modifier = Modifier
                    .weight(0.2f)
                    .fillMaxSize()
                    .padding(10.dp),
            ){
                if(mqttConnectionState == MqttConnectionState.CONNECTING){
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            }
        }
        AnimatedVisibility(mqttConnectionState == MqttConnectionState.CONNECTED) {
            Text(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                text = time.toString(),
                maxLines = 1,
                style = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center)
            )
        }
    }

}

@Preview
@Composable
private fun InfoCardPreview() {
    SensorSpotTheme {
        Surface {
            ConnectionStatusCard(
                mqttConnectionState = MqttConnectionState.CONNECTED,
                time = ElapsedTime(10, 20, 30)
            )
        }
    }
}