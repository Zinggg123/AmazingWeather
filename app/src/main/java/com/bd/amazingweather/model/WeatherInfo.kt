package com.bd.amazingweather.model

data class WeatherInfo(
    val curTemp: String,
    val tempMin: String,
    val tempMax: String,
    val description: String,
    val city:String
)