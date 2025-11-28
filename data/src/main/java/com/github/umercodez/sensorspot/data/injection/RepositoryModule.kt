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

import com.github.umercodez.sensorspot.data.repositories.settings.SettingsRepository
import com.github.umercodez.sensorspot.data.repositories.settings.SettingsRepositoryImp
import com.github.umercodez.sensorspot.data.repositories.settings.sensor.SensorsRepository
import com.github.umercodez.sensorspot.data.repositories.settings.sensor.SensorsRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // ViewModelComponent and ServiceComponent can both access bindings from SingletonComponent.
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        settingsRepositoryImp: SettingsRepositoryImp
    ): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindSensorsRepository(
        sensorsRepositoryImp: SensorsRepositoryImp
    ): SensorsRepository

}
