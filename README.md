<div align="center">

![GitHub License](https://img.shields.io/github/license/UmerCodez/SensorSpot?style=for-the-badge)
   ![Jetpack Compose Badge](https://img.shields.io/badge/Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=fff&style=for-the-badge)


# Sensor Spot
## Android app to stream real-time device sensor data to an MQTT broker. Select sensors, configure broker settings, and publish seamlessly.


<img src="https://github.com/UmerCodez/SensorSpot/blob/main/fastlane/metadata/android/en-US/images/phoneScreenshots/1.jpg" width="250" heigth="250"> <img src="https://github.com/UmerCodez/SensorSpot/blob/main/fastlane/metadata/android/en-US/images/phoneScreenshots/3.jpg" width="250" heigth="250"> <img src="https://github.com/UmerCodez/SensorSpot/blob/main/fastlane/metadata/android/en-US/images/phoneScreenshots/2.jpg" width="250" heigth="250"> <img src="https://github.com/UmerCodez/SensorSpot/blob/main/fastlane/metadata/android/en-US/images/phoneScreenshots/4.jpg" width="250" heigth="250">


</div>

## Usage

1. Configure your broker settings.
2. Press **Connect** to start streaming the selected sensor data in real time.

The data is published to the topic `android/sensor` in the following JSON format:

```json
{
  "type": "android.sensor.accelerometer",
  "timestamp": 3925657519043709,
  "values": [0.31892395, -0.97802734, 10.049896]
}
```

![axis\_device](https://user-images.githubusercontent.com/35717992/179351418-bf3b511a-ebea-49bb-af65-5afd5f464e14.png)

### Explanation

| Array Item | Description                                                 |
| ---------- | ----------------------------------------------------------- |
| values\[0] | Acceleration force along the **x-axis** (including gravity) |
| values\[1] | Acceleration force along the **y-axis** (including gravity) |
| values\[2] | Acceleration force along the **z-axis** (including gravity) |

* **timestamp**: The time (in nanoseconds) when the event occurred. See the [timestamp](https://developer.android.com/reference/android/hardware/SensorEvent#timestamp).

**Note**: For more details about sensor data and what the `values` array represent, refer to the official Android documentation:

* [Motion sensors](https://developer.android.com/guide/topics/sensors/sensors_motion)
* [Position sensors](https://developer.android.com/guide/topics/sensors/sensors_position)
* [Environmental sensors](https://developer.android.com/guide/topics/sensors/sensors_environment)




