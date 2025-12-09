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
package com.github.umercodez.sensorspot.ui.screens.sensors.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.umercodez.sensorspot.data.repositories.settings.sensor.DeviceSensor
import com.github.umercodez.sensorspot.data.repositories.settings.sensor.accelerometer
import com.github.umercodez.sensorspot.ui.SensorSpotTheme

@Composable
fun SensorItem(
    sensor: DeviceSensor,
    dedicatedTopics: Boolean = false,
    mqttTopic: String = "android/sensor",
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit
) {
    var showDetails by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDetails = !showDetails }
        ) {
            ListItem(
                headlineContent = {
                    Text(
                        text = sensor.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                supportingContent = {
                    val subText = if (dedicatedTopics) {
                        "topic = ${mqttTopic}/${if (sensor.stringType.contains(".")) sensor.stringType.split('.').last() else sensor.stringType}"
                    } else {
                        "type = ${sensor.stringType}"
                    }
                    BasicText(
                        text = subText,
                        style = MaterialTheme.typography.bodyLarge,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        autoSize = TextAutoSize.StepBased(
                            minFontSize = 8.sp,
                            maxFontSize = 12.sp,
                        )
                    )
                },
                trailingContent = {
                    Switch(
                        checked = checked,
                        onCheckedChange = onCheckedChange
                    )
                },
                colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent
                )
            )

            AnimatedVisibility(
                visible = showDetails,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HorizontalDivider(
                        modifier = Modifier.padding(bottom = 8.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )

                    SensorDetailRow(label = "Max Range", value = "${sensor.maximumRange}")
                    SensorDetailRow(label = "Min Delay", value = "${sensor.minDelay}")
                    SensorDetailRow(label = "Max Delay", value = "${sensor.maxDelay}")
                    SensorDetailRow(label = "Reporting Mode", value = sensor.reportingModeString)
                    SensorDetailRow(label = "Wake-Up Sensor", value = "${sensor.isWakeUpSensor}")
                    SensorDetailRow(label = "Power", value = "${sensor.power}")
                    SensorDetailRow(label = "Resolution", value = "${sensor.resolution}")
                    SensorDetailRow(label = "Vendor", value = sensor.vendor)
                }
            }
        }
    }
}

@Composable
private fun SensorDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f, fill = false)
        )
    }
}

@Preview
@Composable
private fun SensorItemPreview() {
    SensorSpotTheme {
        Surface {
            SensorItem(
                sensor = accelerometer.copy(stringType = "android.sensor.accelerometer"),
                checked = true,
                onCheckedChange = {}
            )
        }
    }
}
