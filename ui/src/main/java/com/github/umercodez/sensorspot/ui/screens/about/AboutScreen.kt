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
package com.github.umercodez.sensorspot.ui.screens.about

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.umercodez.sensorspot.ui.SensorSpotTheme
import androidx.compose.ui.platform.LocalInspectionMode

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current


        Text(
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            text = "Sensor Spot"
        )
        
        Text(
            text = if (LocalInspectionMode.current) "v1.3.4" else getAppVersion(context),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )


        Spacer(Modifier.height(20.dp))

        HorizontalDivider(Modifier.fillMaxWidth(0.7f))

        Spacer(Modifier.height(50.dp))

        ListItem(
            modifier = Modifier.fillMaxWidth(0.8f),
            headlineContent = { Text("Developed By") },
            supportingContent = { Text("Umer Farooq") },
            leadingContent = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info"
                )
            }
        )

        ListItem(
            modifier = Modifier.fillMaxWidth(0.8f),
            headlineContent = { Text("Feed Back") },
            supportingContent = { Text("umerfarooq2383@gmail.com") },
            leadingContent = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Info"
                )
            }
        )

        ListItem(
            modifier = Modifier.fillMaxWidth(0.8f),
            headlineContent = { Text("Licence") },
            supportingContent = { Text("GPL v3") },
            leadingContent = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info"
                )
            }
        )

    }
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview(){
    SensorSpotTheme {
        AboutScreen()
    }
}

private fun getAppVersion(context: Context) : String{
    val versionName = try {
        context.applicationContext.packageManager
            .getPackageInfo(context.packageName, 0).versionName ?: "Unknown"

    } catch (_: PackageManager.NameNotFoundException) {
        "Unknown"
    }
    return versionName
}