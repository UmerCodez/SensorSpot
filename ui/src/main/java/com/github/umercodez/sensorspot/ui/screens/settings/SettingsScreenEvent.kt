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
package com.github.umercodez.sensorspot.ui.screens.settings

sealed interface SettingsScreenEvent {
    data class OnBrokerAddressChange(val brokerIp: String) : SettingsScreenEvent
    data class OnBrokerPortChange(val brokerPort: Int) : SettingsScreenEvent
    data class OnTopicChange(val topic: String) : SettingsScreenEvent
    data class OnDedicatedTopicsChange(val dedicatedTopics: Boolean) : SettingsScreenEvent
    data class OnQosChange(val qos: Int) : SettingsScreenEvent
    data class OnUseSSLChange(val useSSL: Boolean) : SettingsScreenEvent
    data class OnUseWebsocketChange(val useWebsocket: Boolean) : SettingsScreenEvent
    data class OnUseCredentialsChange(val useCredentials: Boolean) : SettingsScreenEvent
    data class OnConnectionTimeoutChange(val connectionTimeout: Int) : SettingsScreenEvent
    data class OnPasswordChange(val password: String) : SettingsScreenEvent
    data class OnUserNameChange(val userName: String) : SettingsScreenEvent
    data class OnSensorSamplingRateChange(val sensorSamplingRate: Int) : SettingsScreenEvent

    object OnBackClick: SettingsScreenEvent
}