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
package com.github.umercodez.sensorspot.data.sensorpublisherservice

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.github.umercodez.sensorspot.data.R
import com.github.umercodez.sensorspot.data.clock.ElapsedTime
import com.github.umercodez.sensorspot.data.repositories.settings.SettingsRepository
import com.github.umercodez.sensorspot.data.repositories.settings.sensor.SensorsRepository
import com.github.umercodez.sensorspot.data.sensorpublisher.MqttConfig
import com.github.umercodez.sensorspot.data.sensorpublisher.MqttConnectionState
import com.github.umercodez.sensorspot.data.sensorpublisher.SensorPublisher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SensorPublisherServiceImp : Service(), SensorPublisherService {


    @Inject
    lateinit var settingsRepository: SettingsRepository
    @Inject
    lateinit var sensorsRepository: SensorsRepository

    private lateinit var sensorPublisher: SensorPublisher
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)


    override val mqttConnectionState: SharedFlow<MqttConnectionState>
        get() = sensorPublisher.mqttConnectionState

    override val time: SharedFlow<ElapsedTime>
        get() = sensorPublisher.elapsedTime

    private var isConnected: Boolean = false

    private val locationPermissionGranted : Boolean
        get() = !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                applicationContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate()")
        createNotificationChannel()
        sensorPublisher = SensorPublisher(
            context = applicationContext,
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand()")
        trickAndroid8andLater()

        scope.launch {
            connectAndPublish()
        }


        return START_NOT_STICKY
    }


    private suspend fun connectAndPublish() {

        if(isConnected)
            return

        val settings = settingsRepository.settings.first()
        val mqttConfig = MqttConfig.fromSettings(settings)


        scope.launch {
            sensorPublisher.mqttConnectionState.collect{ connectionState ->
                Log.d(TAG, "connection State: $connectionState")
                if(connectionState == MqttConnectionState.Connected){
                    isConnected = true
                    // MainActivity lies in the app module. This service lies in the data module.
                    // To call MainActivity from this module, we use an implicit intent
                    // by specifying the action defined in the AndroidManifest.xml for MainActivity.
                    val notificationIntent = Intent().apply {
                        component = ComponentName(
                            "com.github.umercodez.sensorspot", // Replace with your actual app package name
                            "com.github.umercodez.sensorspot.MainActivity" // Fully qualified name of your MainActivity
                        )
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }

                    val pendingIntent =
                        PendingIntent.getActivity(
                            applicationContext,
                            0,
                            notificationIntent,
                            PendingIntent.FLAG_IMMUTABLE
                        )

                    val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                        .apply {
                            setSmallIcon(R.drawable.flare_24dp)
                            setContentTitle("Publishing")
                            setContentText("connected to ${mqttConfig.brokerAddress}")
                            setPriority(NotificationCompat.PRIORITY_HIGH)
                            setContentIntent(pendingIntent)
                            setAutoCancel(false)
                            setOngoing(true)
                        }


                    val notification = notificationBuilder.build()

                    startForeground(ON_GOING_NOTIFICATION_ID, notification)

                } else if (connectionState == MqttConnectionState.Disconnected || connectionState is MqttConnectionState.ConnectionError || connectionState == MqttConnectionState.ConnectionTimeout){
                    Log.d(TAG, "stopForeground()")
                    stopForeground()
                    isConnected = false
                }
            }
        }

        sensorPublisher.sensorSamplingRate = settings.sensorSamplingRate
        sensorPublisher.connectAndPublish(mqttConfig)

        scope.launch {
            sensorsRepository.getSelectedSensorsAsFlow().collect { selectedSenors ->
                sensorPublisher.sensorIntTypes = selectedSenors.map { it.type }
            }
        }

        scope.launch {
            sensorsRepository.gpsSelectionState.collect { isGpsSelected ->

                if(!locationPermissionGranted)
                    return@collect


                if(isGpsSelected)
                    sensorPublisher.provideGpsData()
                else
                    sensorPublisher.stopProvidingGpsData()
            }
        }

    }

    override fun stopPublishing() {
        scope.launch {
            sensorPublisher.disconnect()
        }

        stopSelf()
    }

    override fun startPublishing() {
        ContextCompat.startForegroundService(applicationContext, Intent(applicationContext, SensorPublisherServiceImp::class.java))
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy()")
        scope.cancel()
        sensorPublisher.cleanUp()
    }

    /**
     * For Android 8 and above there is a framework restriction which required service.startForeground()
     * method to be called within five seconds after call to Context.startForegroundService()
     * so make sure we call this method even if we are returning from service.onStartCommand() without calling
     * service.startForeground()
     */
    private fun trickAndroid8andLater() {
        val tempNotificationId = 521

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val tempNotification = NotificationCompat.Builder(
                applicationContext, CHANNEL_ID
            )
                .setContentTitle("SensorSpot")
                .setContentText("Initializing...").build()

            startForeground(tempNotificationId, tempNotification)
            stopForeground()
        }
    }

    @Suppress("DEPRECATION")
    private fun stopForeground() {
        /*
        If the device is running an older version of Android,
        we fallback to stopForeground(true) to remove the service from the foreground and dismiss the ongoing notification.
        Although it shows as deprecated, it should still work as expected on API level 21 (Android 5).
         */

        // for Android 7 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            stopForeground(STOP_FOREGROUND_REMOVE)
        else
        // This method was deprecated in API level 33.
        // Ignore deprecation message as there is no other alternative method for Android 6 and lower
            stopForeground(true)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "createNotificationChannel()")
            val name: CharSequence = "Sensor Spot"
            val description = "Notifications from Sensor Spot"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Binder given to clients
    private val binder: IBinder = LocalBinder()

    override fun onBind(intent: Intent) = binder

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {


        // Return this instance of LocalService so clients can call public methods
        val service: SensorPublisherServiceImp
            get() = this@SensorPublisherServiceImp // Return this instance of LocalService so clients can call public methods

    }

    companion object {
        private val TAG: String = SensorPublisherServiceImp::class.java.getSimpleName()
        const val CHANNEL_ID = "SensorSpot-Notification-Channel"
        // cannot be zero
        const val ON_GOING_NOTIFICATION_ID = 734
    }
}