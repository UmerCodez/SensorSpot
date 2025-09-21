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
package com.github.umercodez.sensorspot.data.clock

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job
import java.util.Locale

data class ElapsedTime(
    val hours: Int,
    val minutes: Int,
    val seconds: Int
) {
    override fun toString(): String = String.format(locale = Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, seconds)
}

class Clock {
    private var isRunning = false
    private var startTime = 0L
    private var elapsedTime = 0L
    private var timerJob: Job? = null

    private val _time = MutableSharedFlow<ElapsedTime>(replay = 1)
    val time: SharedFlow<ElapsedTime> = _time.asSharedFlow()

    init {
        // Emit initial time
        _time.tryEmit(formatTime(0L))
        startTimer()
    }

    private fun startTimer() {
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                val currentElapsed = if (isRunning) {
                    elapsedTime + (System.currentTimeMillis() - startTime)
                } else {
                    elapsedTime
                }

                _time.emit(formatTime(currentElapsed))
                delay(1000) // Update every second
            }
        }
    }

    fun start() {
        if (!isRunning) {
            startTime = System.currentTimeMillis()
            isRunning = true
        }
    }

    fun stop() {
        if (isRunning) {
            elapsedTime += System.currentTimeMillis() - startTime
            isRunning = false
        }
    }

    fun reset() {
        isRunning = false
        startTime = 0L
        elapsedTime = 0L
        _time.tryEmit(formatTime(0L))
    }

    private fun formatTime(timeInMillis: Long): ElapsedTime {
        val totalSeconds = timeInMillis / 1000
        val hours = (totalSeconds / 3600).toInt()
        val minutes = ((totalSeconds % 3600) / 60).toInt()
        val seconds = (totalSeconds % 60).toInt()

        return ElapsedTime(hours, minutes, seconds)
    }
}