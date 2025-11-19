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
package com.github.umercodez.sensorspot.data.sensoreventprovider

import kotlinx.coroutines.flow.Flow
import org.json.JSONObject


data class SensorEvent(
    val type: String,
    val values: List<Float>,
    val timestamp: Long
){
    fun toJson(includeType: Boolean = true): String {
        val json = mapOf(
            "type" to if (includeType) type else null,
            "values" to values,
            "timestamp" to timestamp
        ).filterValues { it != null }
        return JSONObject(json).toString()
    }
}
interface SensorEventProvider {
    val events: Flow<SensorEvent>
    fun provideEventsFor(sensorTypes : List<Int>, samplingRate: Int = 200000)
    fun stopProvidingEvents()
    fun cleanUp()
}