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

val accelerometer = DeviceSensor(
    name = "Accelerometer",
    stringType = "android.sensor.accelerometer",
    type = 1,
    maximumRange = 19.6133f,
    reportingMode = 0,
    maxDelay = 200000,
    minDelay = 0,
    vendor = "Google",
    power = 0.13f,
    resolution = 0.0029f,
    isWakeUpSensor = false
)

val gyroscope = DeviceSensor(
    name = "Gyroscope",
    stringType = "android.sensor.gyroscope",
    type = 4,
    maximumRange = 34.9066f,
    reportingMode = 0,
    maxDelay = 200000,
    minDelay = 0,
    vendor = "Google",
    power = 0.13f,
    resolution = 0.0011f,
    isWakeUpSensor = false
)

val light = DeviceSensor(
    name = "Light",
    stringType = "android.sensor.light",
    type = 5,
    maximumRange = 40000f,
    reportingMode = 0,
    maxDelay = 200000,
    minDelay = 0,
    vendor = "Google",
    power = 0.13f,
    resolution = 1.0f,
    isWakeUpSensor = false
)

val proximity = DeviceSensor(
    name = "Proximity",
    stringType = "android.sensor.proximity",
    type = 8,
    maximumRange = 5.0f,
    reportingMode = 1, // On-change
    maxDelay = 0,
    minDelay = 0,
    vendor = "Google",
    power = 0.5f,
    resolution = 1.0f,
    isWakeUpSensor = true
)

val magneticField = DeviceSensor(
    name = "Magnetic Field",
    stringType = "android.sensor.magnetic_field",
    type = 2,
    maximumRange = 2000.0f,
    reportingMode = 0,
    maxDelay = 200000,
    minDelay = 0,
    vendor = "Google",
    power = 0.35f,
    resolution = 0.1f,
    isWakeUpSensor = false
)

val fakeSensors = listOf(
    accelerometer,
    gyroscope,
    light,
    proximity,
    magneticField
)




