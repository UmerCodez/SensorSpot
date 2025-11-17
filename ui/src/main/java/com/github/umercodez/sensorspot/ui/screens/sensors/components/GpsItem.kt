package com.github.umercodez.sensorspot.ui.screens.sensors.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.umercodez.sensorspot.ui.SensorSpotTheme


@Composable
fun GpsItem(
    checked: Boolean,
    dedicatedTopics: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    locationPermissionGranted: Boolean = false,
    onGrantLocationPermissionClick: (() -> Unit)? = null
) {
    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clip(RoundedCornerShape(16.dp)),
        headlineContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("GPS")
                Spacer(Modifier.width(10.dp))
                if (!locationPermissionGranted) {
                    TextButton(
                        onClick = { onGrantLocationPermissionClick?.invoke() },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Grant Permission")
                    }
                }

            }
        },
        supportingContent = {
            val prefix = if (dedicatedTopics) "topic = android/sensor/" else "type = "
            Text(
                text = prefix + "android.gps",
                color = MaterialTheme.colorScheme.onSurface
            )
        },

        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = locationPermissionGranted
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    )


}

@Preview
@Composable
fun GpsItemPreview() {
    SensorSpotTheme{
        GpsItem(
            checked = true,
            onCheckedChange = {},
            locationPermissionGranted = true,
            onGrantLocationPermissionClick = {}
        )
    }
}
