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
import com.github.umercodez.sensorspot.data.repositories.settings.SettingsRepository
import com.github.umercodez.sensorspot.data.repositories.settings.SettingsRepositoryImp
import com.github.umercodez.sensorspot.data.repositories.settings.sensor.SensorsRepository
import com.github.umercodez.sensorspot.data.repositories.settings.sensor.SensorsRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ProvideRepoForViewModels {

    @Provides
    @ViewModelScoped
    fun provideSettingsRepository(@ApplicationContext context: Context): SettingsRepository {
        return SettingsRepositoryImp(context)
    }

    @Provides
    @ViewModelScoped
    fun provideSensorsRepository(@ApplicationContext context: Context): SensorsRepository {
        return SensorsRepositoryImp(context)
    }
}

@Module
@InstallIn(ServiceComponent::class)
object ProvideRepoForService {
    @Provides
    @ServiceScoped
    fun provideSettingsRepository(@ApplicationContext context: Context): SettingsRepository {
        return SettingsRepositoryImp(context)
    }

    @Provides
    @ServiceScoped
    fun provideSensorsRepository(@ApplicationContext context: Context): SensorsRepository {
        return SensorsRepositoryImp(context)
    }
}