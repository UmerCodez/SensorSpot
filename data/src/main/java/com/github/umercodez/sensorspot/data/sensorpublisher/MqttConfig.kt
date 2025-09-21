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
package com.github.umercodez.sensorspot.data.sensorpublisher

import com.github.umercodez.sensorspot.data.repositories.settings.Settings


object MqttConfigDefaults{
    const val BROKER_ADDRESS = "broker.hivemq.com"
    const val BROKER_PORT = 1883
    const val QOS = 0
    const val TOPIC = "android/sensor"
    const val CONNECTION_TIMEOUT_SECS = 5
    const val USE_CREDENTIALS = false
    const val USER_NAME = ""
    const val PASSWORD = ""
    const val USE_SSL = false
    const val USE_WEBSOCKET = false
}


data class MqttConfig(
    val brokerAddress: String = MqttConfigDefaults.BROKER_ADDRESS,
    val brokerPort: Int = MqttConfigDefaults.BROKER_PORT,
    val qos: Int = MqttConfigDefaults.QOS,
    val topic: String = MqttConfigDefaults.TOPIC,
    val connectionTimeoutSecs: Int = MqttConfigDefaults.CONNECTION_TIMEOUT_SECS,
    val useCredentials: Boolean = MqttConfigDefaults.USE_CREDENTIALS,
    val userName: String = MqttConfigDefaults.USER_NAME,
    val password: String = MqttConfigDefaults.PASSWORD,
    val useSSL: Boolean = MqttConfigDefaults.USE_SSL,
    val useWebsocket: Boolean = MqttConfigDefaults.USE_WEBSOCKET
){
    companion object {
        fun fromSettings(settings: Settings) : MqttConfig {
            return MqttConfig(
                brokerAddress = settings.brokerAddress,
                brokerPort = settings.brokerPort,
                qos = settings.qos,
                topic = settings.topic,
                connectionTimeoutSecs = settings.connectionTimeoutSecs,
                useCredentials = settings.useCredentials,
                userName = settings.userName,
                password = settings.password,
                useSSL = settings.useSSL,
                useWebsocket = settings.useWebsocket
            )
        }
    }
}
