package com.github.umercodez.sensorspot.data.injection

import android.content.Context
import com.github.umercodez.sensorspot.data.gpsdataprovider.GpsDataProviderImp
import com.github.umercodez.sensorspot.data.sensoreventprovider.SensorEventProviderImp
import com.github.umercodez.sensorspot.data.sensorpublisher.SensorPublisher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob


@Module
@InstallIn(ServiceComponent::class)
object SensorPublisherModule {

    @Provides
    @ServiceScoped
    fun provideServiceCoroutineScope(): CoroutineScope {
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        //Log.d("Hilt", "Creating CoroutineScope: ${System.identityHashCode(scope)}")
        return scope
    }

    @Provides
    @ServiceScoped
    fun provideSensorPublisher(
        @ApplicationContext context: Context,
        scope: CoroutineScope
    ): SensorPublisher {
        //Log.d("Hilt", "SensorPublisher receiving scope: ${System.identityHashCode(scope)}")
        return SensorPublisher(
            scope = scope,
            sensorEventProvider = SensorEventProviderImp(context),
            gpsDataProvider = GpsDataProviderImp(context)
        )
    }

}