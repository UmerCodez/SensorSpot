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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.umercodez.sensorspot.data.repositories.settings.SettingsRepository
import com.github.umercodez.sensorspot.data.sensorpublisher.MqttConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.settings.collect { setting ->
                _uiState.update {
                    it.copy(mqttConfig = MqttConfig.fromSettings(setting), sensorSamplingRate = setting.sensorSamplingRate)
                }

            }
        }
    }

    fun onEvent(event : SettingsScreenEvent){
        when(event) {
            is SettingsScreenEvent.OnBrokerAddressChange -> {
                viewModelScope.launch {
                    settingsRepository.updateSettings {
                        it.copy(brokerAddress = event.brokerIp)
                    }
                }
            }
            is SettingsScreenEvent.OnBrokerPortChange -> {
                viewModelScope.launch {
                    settingsRepository.updateSettings {
                        it.copy(brokerPort = event.brokerPort)
                    }
                }
            }

            is SettingsScreenEvent.OnConnectionTimeoutChange -> {
                viewModelScope.launch {
                    settingsRepository.updateSettings {
                        it.copy(connectionTimeoutSecs = event.connectionTimeout)
                    }
                }
            }
            is SettingsScreenEvent.OnQosChange -> {
                viewModelScope.launch {
                    settingsRepository.updateSettings {
                        it.copy(qos = event.qos)
                    }
                }
            }
            is SettingsScreenEvent.OnTopicChange -> {
                viewModelScope.launch {
                    settingsRepository.updateSettings {
                        it.copy(topic = event.topic)
                    }
                }
            }
            is SettingsScreenEvent.OnDedicatedTopicsChange -> {
                viewModelScope.launch {
                    settingsRepository.updateSettings {
                        it.copy(dedicatedTopics = event.dedicatedTopics)
                    }
                }
            }
            is SettingsScreenEvent.OnUseCredentialsChange -> {
                viewModelScope.launch {
                    settingsRepository.updateSettings {
                        it.copy(useCredentials = event.useCredentials)
                    }
                }
            }
            is SettingsScreenEvent.OnUseSSLChange -> {
                viewModelScope.launch {
                    settingsRepository.updateSettings {
                        it.copy(useSSL = event.useSSL)
                    }
                }
            }
            is SettingsScreenEvent.OnUseWebsocketChange -> {
                viewModelScope.launch {
                    settingsRepository.updateSettings {
                        it.copy(useWebsocket = event.useWebsocket)
                    }
                }
            }

            is SettingsScreenEvent.OnPasswordChange -> {
                viewModelScope.launch {
                    settingsRepository.updateSettings {
                        it.copy(password = event.password)
                    }
                }
            }
            is SettingsScreenEvent.OnUserNameChange -> {
                viewModelScope.launch {
                    settingsRepository.updateSettings {
                        it.copy(userName = event.userName)
                    }
                }
            }
            is SettingsScreenEvent.OnSensorSamplingRateChange -> {
                viewModelScope.launch {
                    settingsRepository.updateSettings {
                        it.copy(sensorSamplingRate = event.sensorSamplingRate)
                    }
                }
            }

            is SettingsScreenEvent.OnBackClick -> {}

        }
    }
}