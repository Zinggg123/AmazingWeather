package com.bd.amazingweather.model

data class WeatherResponse(
    val weather: List<WeatherDesc>, // 天气描述数组
    val main: MainData,             // 主要温度、湿度等信息
    val wind: WindData?,            // 风速风向（可能为空）
    val clouds: CloudData?,         // 云量
    val visibility: Int?,           // 能见度
    val dt: Long,                   // 当前时间戳
    val name: String                // 城市名
)

data class WeatherDesc(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class MainData(
    val temp: Double,               // 当前温度
    val feels_like: Double,         // 体感温度
    val temp_min: Double,           // 最低温度
    val temp_max: Double,           // 最高温度
    val pressure: Int,              // 气压
    val humidity: Int               // 湿度
)

data class WindData(
    val speed: Double,
    val deg: Int
)

data class CloudData(
    val all: Int
)