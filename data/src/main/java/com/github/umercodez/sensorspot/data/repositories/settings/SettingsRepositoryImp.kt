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

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.github.umercodez.sensorspot.data.sensorpublisher.MqttConfigDefaults
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

//The delegate will ensure that we have a single instance of DataStore with that name in our application.
private val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore("settings")


class SettingsRepositoryImp(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SettingsRepository {

    private object Key {
        val BROKER_ADDRESS = stringPreferencesKey("broker_address")
        val BROKER_PORT = intPreferencesKey("broker_port")
        val USERNAME = stringPreferencesKey("username")
        val PASSWORD = stringPreferencesKey("password")
        val USE_SSL = booleanPreferencesKey("use_ssl")
        val USE_WEBSOCKET = booleanPreferencesKey("use_websocket")
        val USE_CREDENTIALS = booleanPreferencesKey("use_credentials")
        val QOS = intPreferencesKey("qos")
        val TOPIC = stringPreferencesKey("topic")
        val DEDICATED_TOPICS = booleanPreferencesKey("dedicated_topics")
        val CONNECTION_TIMEOUT_SECS = intPreferencesKey("connection_timeout_secs")
        val SENSOR_SAMPLING_RATE = intPreferencesKey("sensor_sampling_rate")
    }

    override val settings: Flow<Settings>
        get() = context.userPreferencesDataStore.data.map { pref ->
            Settings(
                brokerAddress = pref[Key.BROKER_ADDRESS] ?: MqttConfigDefaults.BROKER_ADDRESS,
                brokerPort = pref[Key.BROKER_PORT] ?: MqttConfigDefaults.BROKER_PORT,
                userName = pref[Key.USERNAME] ?: MqttConfigDefaults.USER_NAME,
                password = pref[Key.PASSWORD] ?: MqttConfigDefaults.PASSWORD,
                useSSL = pref[Key.USE_SSL] ?: MqttConfigDefaults.USE_SSL,
                useWebsocket = pref[Key.USE_WEBSOCKET] ?: MqttConfigDefaults.USE_WEBSOCKET,
                useCredentials = pref[Key.USE_CREDENTIALS] ?: MqttConfigDefaults.USE_CREDENTIALS,
                qos = pref[Key.QOS] ?: MqttConfigDefaults.QOS,
                topic = pref[Key.TOPIC] ?: MqttConfigDefaults.TOPIC,
                dedicatedTopics = pref[Key.DEDICATED_TOPICS] ?: MqttConfigDefaults.DEDICATED_TOPICS,
                connectionTimeoutSecs = pref[Key.CONNECTION_TIMEOUT_SECS]
                    ?: MqttConfigDefaults.CONNECTION_TIMEOUT_SECS,
                sensorSamplingRate = pref[Key.SENSOR_SAMPLING_RATE]
                    ?: 200000
            )
        }.flowOn(ioDispatcher)

    override suspend fun updateSettings(settings: (settings: Settings) -> Settings) {
        val oldSettings = this.settings.first()
        val newSettings = settings.invoke(oldSettings)
        saveSettings(newSettings)
    }

    private suspend fun saveSettings(settings: Settings) {

        context.userPreferencesDataStore.edit { pref ->
            pref[Key.BROKER_ADDRESS] = settings.brokerAddress
            pref[Key.BROKER_PORT] = settings.brokerPort
            pref[Key.USERNAME] = settings.userName
            pref[Key.PASSWORD] = settings.password
            pref[Key.USE_SSL] = settings.useSSL
            pref[Key.USE_WEBSOCKET] = settings.useWebsocket
            pref[Key.USE_CREDENTIALS] = settings.useCredentials
            pref[Key.QOS] = settings.qos
            pref[Key.TOPIC] = settings.topic
            pref[Key.DEDICATED_TOPICS] = settings.dedicatedTopics
            pref[Key.CONNECTION_TIMEOUT_SECS] = settings.connectionTimeoutSecs
        }

    }
}