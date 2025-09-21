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
package com.github.umercodez.sensorspot.data.repositories.settings.sensor


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager

// Wrapper class for android.hardware.Sensor
// Directly using android.hardware.Sensor or other context-dependent APIs within a Composable function
// can prevent Android Studio from rendering previews. This is because the preview environment might not have access to these features (e.g., sensors, camera)
data class DeviceSensor(
    val name: String,
    val stringType: String,
    val type: Int,
    val maximumRange: Float,
    val reportingMode: Int,
    val maxDelay: Int,
    val minDelay: Int,
    val vendor: String,
    val power: Float,
    val resolution: Float,
    val isWakeUpSensor: Boolean,
){
    val reportingModeString: String
        get() = when (reportingMode) {
            Sensor.REPORTING_MODE_CONTINUOUS -> "Continuous"
            Sensor.REPORTING_MODE_ON_CHANGE -> "On Change"
            Sensor.REPORTING_MODE_ONE_SHOT -> "One Shot"
            Sensor.REPORTING_MODE_SPECIAL_TRIGGER -> "Special Trigger"
            else -> "Unknown"
        }
    fun toSensor(context : Context) : Sensor {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        return sensorManager.getDefaultSensor(this.type) ?: throw IllegalArgumentException("Sensor not found")
    }
}

fun Sensor.toDeviceSensor() : DeviceSensor {
    return DeviceSensor(
        name = this.name,
        stringType = this.stringType,
        type = this.type,
        maximumRange = this.maximumRange,
        reportingMode = this.reportingMode,
        maxDelay = this.maxDelay,
        minDelay = this.minDelay,
        vendor = this.vendor,
        power = this.power,
        resolution = this.resolution,
        isWakeUpSensor = this.isWakeUpSensor
    )
}


fun List<Sensor>.toDeviceSensors() = this.map { it.toDeviceSensor() }
fun List<DeviceSensor>.toSensors(context : Context) = this.map { it.toSensor(context) }
