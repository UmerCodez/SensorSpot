package com.github.umercodez.sensorspot.data.gpsdataprovider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


class GpsDataProviderImp(
    private val context: Context
) : GpsDataProvider, LocationListener{



    private val scope : CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _gpsEvent = MutableSharedFlow<GpsData>()
    private var handlerThread: HandlerThread = HandlerThread("Handler Thread")
    private var handler: Handler
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private var isStreaming = false

    init {
        handlerThread.start()
        handler = Handler(handlerThread.looper)
    }

    override val gpsData: SharedFlow<GpsData>
        get() = _gpsEvent.asSharedFlow()

    //@RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    @SuppressLint("MissingPermission")
    override fun startProvidingGpsData() {

        // For Android 6.0 or later
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        )
            return


        // In Android 5.0 permissions are granted at installation time
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            0f,
            this,
            handlerThread.looper
        )

        isStreaming = true

    }

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun stopProvidingGpsData() {
        if(isStreaming){
            locationManager.removeUpdates( this )
            isStreaming = false
        }
    }

    override fun onLocationChanged(location: Location) {
        scope.launch {
            _gpsEvent.emit(
                GpsData(
                    longitude = location.longitude,
                    latitude = location.latitude,
                    altitude = location.altitude,
                    accuracy = location.accuracy,
                    speed = location.speed,
                    time = location.time,
                    bearing = location.bearing
                )
            )
        }
    }

    // See issue  : https://github.com/UmerCodez/SensorSpot/issues/10
    // solution : https://stackoverflow.com/questions/64638260/android-locationlistener-abstractmethoderror-on-onstatuschanged-and-onproviderd
    @Deprecated("Deprecated in Java")
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        // super.onStatusChanged(provider, status, extras)
    }
    override fun onProviderEnabled(provider: String) {
    }

    override fun onProviderDisabled(provider: String) {
    }

    override fun cleanUp() {
        if(handlerThread.isAlive)
            handlerThread.quitSafely()

        try{
            scope.cancel()
        }catch (e: IllegalStateException){
            e.printStackTrace()
        }

        isStreaming = false
    }
}