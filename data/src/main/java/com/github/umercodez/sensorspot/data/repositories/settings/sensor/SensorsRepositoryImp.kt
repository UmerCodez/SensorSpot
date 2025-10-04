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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

//The delegate will ensure that we have a single instance of DataStore with that name in our application.
private val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore("sensors")

class SensorsRepositoryImp(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SensorsRepository {

    private object Key{
        val SELECTED_SENSORS = stringPreferencesKey("selected_sensors")
        val GPS_ENABLED = booleanPreferencesKey("gps_enabled")
    }


    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    override fun getAvailableSensors(): List<DeviceSensor> {
        return sensorManager.getSensorList(Sensor.TYPE_ALL).filter{ it.reportingMode != Sensor.REPORTING_MODE_ONE_SHOT}.toDeviceSensors()
    }

    override fun getSensor(sensorType: Int): DeviceSensor {
        return sensorManager.getDefaultSensor(sensorType)?.toDeviceSensor() ?: throw IllegalArgumentException("Sensor not found")
    }

    override suspend fun saveSelectedSensors(sensors: List<DeviceSensor>) {
        context.userPreferencesDataStore.edit { pref ->
            pref[Key.SELECTED_SENSORS] = Json.encodeToString(sensors.map { it.type }.toList())
        }
    }

    override suspend fun getSelectedSensors(): List<DeviceSensor> {
        return context.userPreferencesDataStore.data.map { pref ->
            Json.decodeFromString<List<Int>>(pref[Key.SELECTED_SENSORS] ?: "[]")
                .map { getSensor(it) }.toList()
        }.flowOn(ioDispatcher).first()
    }

    override fun getSelectedSensorsAsFlow() : Flow<List<DeviceSensor>> {
        return context.userPreferencesDataStore.data.map { pref ->
            Json.decodeFromString<List<Int>>(pref[Key.SELECTED_SENSORS] ?: "[]")
                .map { getSensor(it) }.toList()
        }.flowOn(ioDispatcher)
    }


    override suspend fun saveGpsSelectionState(state: Boolean) {
        context.userPreferencesDataStore.edit { pref ->
            pref[Key.GPS_ENABLED] = state
        }
    }

    override val gpsSelectionState : Flow<Boolean>
        get()  = context.userPreferencesDataStore.data.map { pref ->
            pref[Key.GPS_ENABLED] ?: false
        }
}