package com.GreenatoSolarini.myapplicationjetpackcompose.api

import com.GreenatoSolarini.myapplicationjetpackcompose.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("v1/forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("current") current: String = "temperature_2m,weather_code,wind_speed_10m",
        @Query("hourly") hourly: String = "shortwave_radiation",
        @Query("timezone") timezone: String = "America/Santiago"
    ): WeatherResponse
}