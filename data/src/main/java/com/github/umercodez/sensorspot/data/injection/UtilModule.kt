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
package com.github.umercodez.sensorspot.data.injection

import android.content.Context
import com.github.umercodez.sensorspot.data.sensorpublisherservice.SensorPublisherServiceBindHelper
import com.github.umercodez.sensorspot.data.sensorpublisherservice.SensorPublisherServiceBindHelperImp
import com.github.umercodez.sensorspot.data.utils.LocationPermissionUtil
import com.github.umercodez.sensorspot.data.utils.LocationPermissionUtilImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UtilModule {

    @Provides
    @ViewModelScoped
    fun provideSensorPublisherServiceBindHelper(@ApplicationContext context: Context): SensorPublisherServiceBindHelper {
        return SensorPublisherServiceBindHelperImp(context)
    }

    @Provides
    @ViewModelScoped
    fun provideLocationPermissionUtil(@ApplicationContext context: Context): LocationPermissionUtil {
        return LocationPermissionUtilImp(context)
    }
}