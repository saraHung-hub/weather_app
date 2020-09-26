package com.sara.sara_weather.ui.Server

import com.sara.sara_weather.ui.Model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("data/2.5/weather?")
    fun getWeaterByGeo(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("APPID") app_id: String?
    ): Call<WeatherResponse?>?

    @GET("data/2.5/weather?")
    fun getWeaterByZip(
        @Query("zip") zip: Int?,
        @Query("APPID") app_id: String?
    ): Call<WeatherResponse?>?

    @GET("data/2.5/weather?")
    fun getWeaterByCity(
        @Query("q") city: String?,
        @Query("APPID") app_id: String?
    ): Call<WeatherResponse?>?
}
