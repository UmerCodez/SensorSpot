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
package com.github.umercodez.sensorspot.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.umercodez.sensorspot.data.repositories.settings.sensor.fakeSensors
import com.github.umercodez.sensorspot.ui.SensorSpotTheme
import com.github.umercodez.sensorspot.ui.screens.about.AboutScreen
import com.github.umercodez.sensorspot.ui.screens.publish.PublishScreen
import com.github.umercodez.sensorspot.ui.screens.publish.PublishScreenState
import com.github.umercodez.sensorspot.ui.screens.sensors.SensorsScreen
import com.github.umercodez.sensorspot.ui.screens.sensors.SensorsScreenState
import com.github.umercodez.sensorspot.ui.screens.settings.SettingsScreen
import com.github.umercodez.sensorspot.ui.screens.settings.SettingsScreenState
import kotlinx.serialization.Serializable

object Route {
    @Serializable
    object PublishScreen

    @Serializable
    object SensorsScreen

    @Serializable
    object SettingsScreen

    @Serializable
    object AboutScreen
}

sealed class NavItem(val route: Any, val icon: ImageVector, val label: String) {
    data object Publish : NavItem(Route.PublishScreen, Icons.Filled.Home, "Publish")
    data object Sensors : NavItem(Route.SensorsScreen, Icons.AutoMirrored.Filled.List, "Sensors")
    data object Settings : NavItem(Route.SettingsScreen, Icons.Filled.Settings, "Settings")
    data object About : NavItem(Route.AboutScreen, Icons.Filled.Info, "About")
}

private val navItems = listOf(
    NavItem.Publish,
    NavItem.Sensors,
    NavItem.Settings,
    NavItem.About
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    var selectedNavItemIndex by remember { mutableIntStateOf(navItems.indexOf(NavItem.Publish)) }
    val navController = rememberNavController()

    Scaffold(

        bottomBar = {

            NavigationBar {

                navItems.forEach { navItem ->
                    NavigationBarItem(
                        selected = navItems.indexOf(navItem) == selectedNavItemIndex,
                        onClick = {
                            selectedNavItemIndex = navItems.indexOf(navItem)
                            navController.navigate(navItem.route){

                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                        },
                        label = { Text(text = navItem.label) }
                    )
                }

            }
        }
        
    ) { innerPadding ->

        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            navController = navController,
            startDestination = Route.PublishScreen
        ) {
            composable<Route.PublishScreen> {
                if (!LocalInspectionMode.current) {
                    PublishScreen()
                } else {
                    PublishScreen(
                        state = PublishScreenState(),
                        onEvent = {}
                    )
                }
            }
            composable<Route.SensorsScreen> {
                if (!LocalInspectionMode.current) {
                    SensorsScreen()
                } else {
                    SensorsScreen(
                        state = SensorsScreenState(
                            allSensors = fakeSensors.toMutableStateList()
                        ),
                        onEvent = {}
                    )
                }


            }

            composable<Route.SettingsScreen> {
                if (!LocalInspectionMode.current) {
                    SettingsScreen()
                } else {
                    SettingsScreen(
                        state = SettingsScreenState(),
                        onEvent = {}
                    )
                }
            }

            composable<Route.AboutScreen> {
                AboutScreen()
            }

        }

    }

}

@Preview
@Composable
fun HomeScreenPreview() {
    SensorSpotTheme {
        HomeScreen()
    }
}