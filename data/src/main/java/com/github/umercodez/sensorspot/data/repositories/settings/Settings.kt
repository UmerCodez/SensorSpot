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
package com.github.umercodez.sensorspot.data.repositories.settings


object SettingsDefault {
    const val SENSOR_SAMPLING_RATE = 200000
}


data class Settings(
    val brokerAddress: String,
    val brokerPort: Int,
    val qos: Int,
    val topic: String,
    val connectionTimeoutSecs: Int,
    val useCredentials: Boolean,
    val userName: String,
    val password: String,
    val useSSL: Boolean,
    val useWebsocket: Boolean,
    val sensorSamplingRate: Int
)