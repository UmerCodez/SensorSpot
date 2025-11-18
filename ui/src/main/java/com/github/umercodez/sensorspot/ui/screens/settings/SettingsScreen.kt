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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults.itemShape
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.umercodez.sensorspot.ui.SensorSpotTheme
import com.github.umercodez.sensorspot.ui.screens.settings.components.EditTextPref

@Composable
fun SettingsScreen(
    viewModel: SettingsScreenViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: SettingsScreenState,
    onEvent: (SettingsScreenEvent) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        EditTextPref(
            value = state.mqttConfig.brokerAddress,
            title = { Text("Broker Address") },
            isError = { value ->
                value.isEmpty()
            },
            onUpdateClick = { onEvent(SettingsScreenEvent.OnBrokerAddressChange(it)) }
        )

        EditTextPref(
            value = state.mqttConfig.brokerPort.toString(),
            title = { Text("Broker Port") },
            isError = { value ->
                value.isEmpty() || value.toIntOrNull() == null || value.toInt() !in 1..65535
            },
            onUpdateClick = { onEvent(SettingsScreenEvent.OnBrokerPortChange(it.toInt())) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        EditTextPref(
            value = state.mqttConfig.topic,
            title = { Text("Topic") },
            isError = { value ->
                value.isEmpty()
            },
            onUpdateClick = { onEvent(SettingsScreenEvent.OnTopicChange(it)) }
        )

        ListItem(
            headlineContent = { Text("Dedicated topics") },
            supportingContent = { Text("use different topic for each sensor") },
            trailingContent = {
                Switch(
                    checked = state.mqttConfig.dedicatedTopics,
                    onCheckedChange = { onEvent(SettingsScreenEvent.OnDedicatedTopicsChange(it)) }
                )
            }
        )

        ListItem(
            headlineContent = { Text("Qos") },
            trailingContent = {
                SingleChoiceSegmentedButtonRow {
                    val qosOptions = listOf(0, 1, 2)
                    qosOptions.forEachIndexed { index, qosValue ->
                        SegmentedButton(
                            selected = state.mqttConfig.qos == qosValue,
                            onClick = { onEvent(SettingsScreenEvent.OnQosChange(qosValue)) },
                            shape = itemShape(index = index, count = qosOptions.size)
                        ) {
                            Text(qosValue.toString())
                        }
                    }
                }
            }
        )

        EditTextPref(
            value = state.mqttConfig.connectionTimeoutSecs.toString(),
            title = { Text("Connection Timeout") },
            isError = { value ->
                value.isEmpty() || value.toIntOrNull() == null || value.toInt() !in 5..20
            },
            errorText = "Range: 5-20s",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            onUpdateClick = { onEvent(SettingsScreenEvent.OnConnectionTimeoutChange(it.toInt())) },

            )

        EditTextPref(
            value = state.sensorSamplingRate.toString(),
            title = { Text("Sensor Sampling Rate") },
            isError = { value ->
                value.isEmpty() || value.toIntOrNull() == null || value.toInt() !in 0..200000
            },
            errorText = "Range: 0-200000",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            onUpdateClick = { onEvent(SettingsScreenEvent.OnSensorSamplingRateChange(it.toInt())) },


            )

        ListItem(
            headlineContent = { Text("Websocket") },
            supportingContent = { Text("use websocket") },
            trailingContent = {
                Switch(
                    checked = state.mqttConfig.useWebsocket,
                    onCheckedChange = { onEvent(SettingsScreenEvent.OnUseWebsocketChange(it)) }
                )
            }
        )

        ListItem(
            headlineContent = { Text("SSL") },
            supportingContent = { Text("use ssl") },
            trailingContent = {
                Switch(
                    checked = state.mqttConfig.useSSL,
                    onCheckedChange = { onEvent(SettingsScreenEvent.OnUseSSLChange(it)) }
                )
            }
        )

        ListItem(
            headlineContent = { Text("Credentials") },
            trailingContent = {
                Switch(
                    checked = state.mqttConfig.useCredentials,
                    onCheckedChange = { onEvent(SettingsScreenEvent.OnUseCredentialsChange(it)) }
                )
            }
        )

        AnimatedVisibility(state.mqttConfig.useCredentials) {
            Column {
                EditTextPref(
                    value = state.mqttConfig.userName,
                    title = { Text("Username") },
                    isError = { value ->
                        value.isEmpty()
                    },
                    onUpdateClick = { onEvent(SettingsScreenEvent.OnUserNameChange(it)) }
                )

                EditTextPref(
                    value = state.mqttConfig.password,
                    title = { Text("Password") },
                    password = true,
                    isError = { value ->
                        value.isEmpty()
                    },
                    onUpdateClick = { onEvent(SettingsScreenEvent.OnPasswordChange(it)) }
                )
            }
        }
    }

}

@Preview
@Composable
fun SettingsScreenPreview() {
    SensorSpotTheme {
        SettingsScreen(
            state = SettingsScreenState(),
            onEvent = {}
        )
    }
}
