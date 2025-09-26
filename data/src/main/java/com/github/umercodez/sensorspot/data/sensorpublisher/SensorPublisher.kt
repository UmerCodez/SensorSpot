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

import android.content.Context
import android.util.Log
import com.github.umercodez.sensorspot.data.clock.Clock
import com.github.umercodez.sensorspot.data.clock.ElapsedTime
import com.github.umercodez.sensorspot.data.sensoreventprovider.SensorEventProvider
import com.github.umercodez.sensorspot.data.sensoreventprovider.SensorEventProviderImp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.eclipse.paho.mqttv5.client.IMqttToken
import org.eclipse.paho.mqttv5.client.MqttActionListener
import org.eclipse.paho.mqttv5.client.MqttAsyncClient
import org.eclipse.paho.mqttv5.client.MqttCallback
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse

import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence
import org.eclipse.paho.mqttv5.common.MqttException
import org.eclipse.paho.mqttv5.common.MqttMessage
import org.eclipse.paho.mqttv5.common.packet.MqttProperties
import java.net.SocketTimeoutException
import java.util.UUID


enum class MqttConnectionState{
    CONNECTING,
    CONNECTED,
    DISCONNECTED,
    CONNECTION_ERROR,
    CONNECTION_TIMEOUT
}

class SensorPublisher(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : MqttCallback {

    private val tag = "SensorPublisher"


    private var mqttAsyncClient: MqttAsyncClient? = null
    private var memoryPersistence: MemoryPersistence? = null
    private var connectionOptions: MqttConnectionOptions? = null
    private var scope: CoroutineScope? = null
    private var sensorEventProvider: SensorEventProvider? = null
    private val clock = Clock()
    val elapsedTime : SharedFlow<ElapsedTime> get() = clock.time
    var sensorSamplingRate: Int = 200000

    var sensorIntTypes: List<Int>
        set(sensorTypes) {
            sensorEventProvider?.stopProvidingEvents()
            sensorEventProvider?.provideEventsFor(sensorTypes, sensorSamplingRate)
        }
        get() = sensorIntTypes

    private val _mqttConnectionState = MutableSharedFlow<MqttConnectionState>(replay = 1)
    val mqttConnectionState = _mqttConnectionState.asSharedFlow()


    suspend fun connectAndPublish(mqttConfig: MqttConfig) = withContext(ioDispatcher){

        if(scope == null) {
            scope = CoroutineScope(SupervisorJob() + ioDispatcher)
        }

        if(sensorEventProvider == null){
            sensorEventProvider = SensorEventProviderImp(context)
        }

        scope?.launch {
            sensorEventProvider?.events?.collect{ json: String ->

                try {

                    if(mqttAsyncClient?.isConnected == true) {
                        val message = MqttMessage(json.toByteArray()).apply {
                            qos = mqttConfig.qos
                        }
                        mqttAsyncClient?.publish(mqttConfig.topic, message)
                    }

                } catch (e: MqttException) {
                    e.printStackTrace()
                }
            }
        }

        _mqttConnectionState.emit(MqttConnectionState.CONNECTING)

        val broker = if (mqttConfig.useSSL) {
            if (mqttConfig.useWebsocket)
                "wss://${mqttConfig.brokerAddress}:${mqttConfig.brokerPort}"
            else
                "ssl://${mqttConfig.brokerAddress}:${mqttConfig.brokerPort}"
        } else {
            if (mqttConfig.useWebsocket)
                "ws://${mqttConfig.brokerAddress}:${mqttConfig.brokerPort}"
            else
                "tcp://${mqttConfig.brokerAddress}:${mqttConfig.brokerPort}"
        }
        memoryPersistence = MemoryPersistence()
        connectionOptions = MqttConnectionOptions()

        try {

            mqttAsyncClient = MqttAsyncClient(broker, UUID.randomUUID().toString(), memoryPersistence)


            connectionOptions?.apply {
                isAutomaticReconnect = false
                isCleanStart = false
                connectionTimeout = mqttConfig.connectionTimeoutSecs


                if(mqttConfig.useCredentials){
                    userName = mqttConfig.userName
                    password = mqttConfig.password.toByteArray()
                }

            }

            mqttAsyncClient?.setCallback(this@SensorPublisher)
            mqttAsyncClient?.connect(connectionOptions,null,object: MqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    println(asyncActionToken)
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {

                    if(exception is SocketTimeoutException) {
                        scope?.launch {
                            _mqttConnectionState.emit(MqttConnectionState.CONNECTION_TIMEOUT)
                        }
                    }

                    else {
                        scope?.launch {
                            _mqttConnectionState.emit(MqttConnectionState.CONNECTION_ERROR)
                        }
                    }
                    exception?.printStackTrace()

                }

            })

        } catch (e: MqttException) {
            e.printStackTrace()
            scope?.launch {
                _mqttConnectionState.emit(MqttConnectionState.CONNECTION_ERROR)
            }
        }

    }

    override fun connectComplete(reconnect: Boolean, serverURI: String?) {
        Log.d(tag, "connectComplete()")
        clock.start()
        scope?.launch {
            _mqttConnectionState.emit(MqttConnectionState.CONNECTED)
        }
        sensorEventProvider?.provideEventsFor(sensorIntTypes,sensorSamplingRate)
    }

    override fun disconnected(disconnectResponse: MqttDisconnectResponse?) {
        Log.d(tag, "disconnected()")
        clock.reset()
        scope?.launch {
            _mqttConnectionState.emit(MqttConnectionState.DISCONNECTED)
        }
        sensorEventProvider?.stopProvidingEvents()
        disconnectResponse?.reasonString?.also { println(it) }
    }

    override fun mqttErrorOccurred(exception: MqttException?) {
        Log.d(tag, "mqttErrorOccurred()")
        clock.reset()
        scope?.launch {
            _mqttConnectionState.emit(MqttConnectionState.CONNECTION_ERROR)
        }
        exception?.printStackTrace()
    }

    override fun messageArrived(topic: String?, message: MqttMessage?) {

    }

    override fun deliveryComplete(token: IMqttToken?) {

    }

    override fun authPacketArrived(reasonCode: Int, properties: MqttProperties?) {

    }

    suspend fun close() : Unit = withContext(ioDispatcher) {
        try {

            mqttAsyncClient?.disconnect()
            mqttAsyncClient?.close()
            mqttAsyncClient = null
            _mqttConnectionState.emit(MqttConnectionState.DISCONNECTED)
            sensorEventProvider?.stopProvidingEvents()
            sensorEventProvider?.cleanUp()
            sensorEventProvider = null
            scope?.cancel()
            scope = null
            clock.reset()

        } catch (e: Exception) {
            e.printStackTrace()
            _mqttConnectionState.emit(MqttConnectionState.CONNECTION_ERROR)
        }
    }

}


