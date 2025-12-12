package com.greenatosolarini.myapplicationjetpackcompose.model

data class WeatherResponse(
    val current: CurrentWeather,
    val hourly: HourlyWeather
)

data class CurrentWeather(
    val temperature_2m: Double,
    val weather_code: Int,
    val wind_speed_10m: Double
)

data class HourlyWeather(
    val time: List<String>,
    val shortwave_radiation: List<Double>
)
