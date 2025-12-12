package com.greenatosolarini.myapplicationjetpackcompose.repository

import com.greenatosolarini.myapplicationjetpackcompose.api.WeatherApiService
import com.greenatosolarini.myapplicationjetpackcompose.model.WeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val weatherApiService: WeatherApiService = retrofit.create(WeatherApiService::class.java)

    suspend fun getWeather(lat: Double = -33.45, lon: Double = -70.67): WeatherResponse {
        return weatherApiService.getCurrentWeather(lat, lon)
    }
}
