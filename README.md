<div align="center">

![GitHub License](https://img.shields.io/github/license/UmerCodez/SensorSpot?style=for-the-badge)
   ![Jetpack Compose Badge](https://img.shields.io/badge/Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=fff&style=for-the-badge) ![MQTT](https://img.shields.io/badge/protocol-mqtt_v5-green?style=for-the-badge) ![Android](https://img.shields.io/badge/Android%205.0+-3DDC84?style=for-the-badge&logo=android&logoColor=white) ![GitHub Release](https://img.shields.io/github/v/release/UmerCodez/SensorSpot?include_prereleases&style=for-the-badge) ![Material 3](https://img.shields.io/badge/Material%203-ebe89d?style=for-the-badge&logo=materialdesign&logoColor=white)

[<img src="https://github.com/user-attachments/assets/0f628053-199f-4587-a5b2-034cf027fb99" height="100">](https://github.com/UmerCodez/SensorSpot/releases) [<img src="https://github.com/user-attachments/assets/6d0d2095-876c-4248-89d5-43b53ad21c35" height="100">](https://f-droid.org/packages/com.github.umercodez.sensorspot/) [<img src="https://github.com/user-attachments/assets/f311c689-cfd1-4326-8e7d-323e2e117006" height="100">](https://apt.izzysoft.de/fdroid/index/apk/com.github.umercodez.sensorspot)  


# Sensor Spot
## Android app to stream real-time device sensor data to an [MQTT](https://mqtt.org/) broker. Select sensors, configure broker settings, and publish seamlessly.


<img src="https://github.com/UmerCodez/SensorSpot/blob/main/fastlane/metadata/android/en-US/images/phoneScreenshots/1.jpg" width="250" heigth="250"> <img src="https://github.com/UmerCodez/SensorSpot/blob/main/fastlane/metadata/android/en-US/images/phoneScreenshots/3.jpg" width="250" heigth="250"> <img src="https://github.com/UmerCodez/SensorSpot/blob/main/fastlane/metadata/android/en-US/images/phoneScreenshots/2.jpg" width="250" heigth="250"> <img src="https://github.com/UmerCodez/SensorSpot/blob/main/fastlane/metadata/android/en-US/images/phoneScreenshots/4.jpg" width="250" heigth="250">


</div>

⚠️ _The App only supports **MQTT v5** for now_

## Usage

1. Configure your broker settings.
2. Press **Connect** to start streaming the selected sensor data in real time.
3. App allows user to select or deselect sensors **while connected**, with the published data automatically updating to reflect the new choices.


The data is published to the topic `android/sensor` in the following JSON format:

```json
{
  "type": "android.sensor.accelerometer",
  "timestamp": 3925657519043709,
  "values": [0.31892395, -0.97802734, 10.049896]
}
```
Note: If the **Dedicated Topics** option is enabled in the settings, the `type` field **will not be included** in the JSON payload. Each sensor will instead publish to its own topic. For example, accelerometer data will be sent to `android/sensor/accelerometer`.

<div align="center">
   
<img width="282" height="341" alt="image" src="https://github.com/user-attachments/assets/4742b447-3713-417a-b3aa-d06903f30a7a" />

</div>

### Explanation

| Array Item | Description                                                 |
| ---------- | ----------------------------------------------------------- |
| values\[0] | Acceleration force along the **x-axis** (including gravity) |
| values\[1] | Acceleration force along the **y-axis** (including gravity) |
| values\[2] | Acceleration force along the **z-axis** (including gravity) |

* **timestamp**: The time (in nanoseconds) when the event occurred. See the [timestamp](https://developer.android.com/reference/android/hardware/SensorEvent#timestamp).

**Note**: For more details about other sensors data and what the `values` array represent, refer to the official Android documentation:

* [Motion sensors](https://developer.android.com/guide/topics/sensors/sensors_motion)
* [Position sensors](https://developer.android.com/guide/topics/sensors/sensors_position)
* [Environmental sensors](https://developer.android.com/guide/topics/sensors/sensors_environment)

  
## GPS data format
GPS data is sent in following json format
```json
{
  "type": "android.gps",
  "longitude": 155.9011,
  "latitude": 81.4278,
  "altitude": 217.5,
  "bearing": 135.0,
  "accuracy": 5.2,
  "speed": 2.8,
  "time": 1727845963000
}
```
where:

| Field       | Description                                                                                                            |
|-------------|------------------------------------------------------------------------------------------------------------------------|
| longitude   | Longitude in degrees.                                                                                                 |
| latitude    | Latitude in degrees.                                                                                                  |
| altitude    | The altitude of the location in meters above the WGS84 reference ellipsoid.                                         |
| bearing     | Bearing at the time of this location in degrees. Bearing is the horizontal direction of travel and is unrelated to device orientation. The bearing is guaranteed to be in the range \[0, 360). |
| accuracy    | Estimated horizontal accuracy radius in meters of this location at the 68th percentile confidence level.           |
| speed       | Speed at the time of this location in meters per second.                                                            |
| time        | The Unix epoch time of this location fix, in milliseconds since the start of the Unix epoch (00:00:00 January 1, 1970 UTC). |

### Limitation on Android 14+ devices
Starting with Android 14, background execution restrictions are more strict. When you connect to the broker, the app starts a foreground service to publish sensor data in real time. However, if you leave the app for more than 5 seconds (e.g., switch to another app or close it), the system may stop this foreground service, causing a disconnection from the broker. This occurs even if background activity is allowed for the app.

To avoid this issue, keep the app running in the foreground while connected.

## ⭐️ Support

Appreciate the project? Here's how you can help:

1. **Star**: Give it a star at the top right. It means a lot!
2. **Contribute**: Found an issue or have a feature idea? Submit a PR.
3. **Feedback**: Have suggestions? Open an issue or start a discussion.




