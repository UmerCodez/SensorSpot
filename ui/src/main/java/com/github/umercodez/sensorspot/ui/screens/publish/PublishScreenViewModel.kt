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
package com.github.umercodez.sensorspot.ui.screens.publish

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.umercodez.sensorspot.data.repositories.settings.SettingsRepository
import com.github.umercodez.sensorspot.data.sensorpublisher.MqttConfig
import com.github.umercodez.sensorspot.data.sensorpublisherservice.SensorPublisherService
import com.github.umercodez.sensorspot.data.sensorpublisherservice.SensorPublisherServiceBindHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PublishScreenViewModel @Inject constructor(
    private val sensorPublisherServiceBinderHelper: SensorPublisherServiceBindHelper,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PublishScreenState())
    val uiState = _uiState.asStateFlow()

    val tag = "PublishScreenViewModel"

    private var sensorPublisherService: SensorPublisherService? = null

    init {

        Log.d(tag, "init ${hashCode()}")

        viewModelScope.launch {
            settingsRepository.settings.map { MqttConfig.fromSettings(it) }.collect{ mqttConfig ->
                _uiState.update {
                    it.copy(mqttConfig = mqttConfig)
                }
            }
        }


        sensorPublisherServiceBinderHelper.onSensorPublisherServiceConnected { service ->

            Log.d(tag, "onSensorPublisherServiceConnected()")
            sensorPublisherService = service

            viewModelScope.launch {
                sensorPublisherService?.mqttConnectionState?.collect { mqttConnectionState ->
                    _uiState.update {
                        it.copy(mqttConnectionState = mqttConnectionState)
                    }
                }
            }

            viewModelScope.launch {

                sensorPublisherService?.time?.collect{ clockTime ->
                    _uiState.update {
                        it.copy(elapsedTime = clockTime)
                    }
                }
            }

        }

        sensorPublisherServiceBinderHelper.bindToSensorPublisherService()
    }

    fun onEvent(event: PublishScreenEvent) {
        when (event) {
            is PublishScreenEvent.OnPublishClick -> {
                sensorPublisherService?.startPublishing()
            }

            PublishScreenEvent.OnStopClick -> {
                sensorPublisherService?.stopPublishing()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        sensorPublisherServiceBinderHelper.unBindFromSensorPublisherService()
        Log.d(tag, "onCleared() ${hashCode()}")
    }

}
