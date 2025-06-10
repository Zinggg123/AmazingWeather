package com.bd.amazingweather.model

import android.database.Cursor
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

object WeatherRepository {
    private val client = OkHttpClient()
    private val gson = Gson()

    suspend fun getTianjinWeather(): WeatherInfo {
        delay(500) // 模拟网络延迟
        return WeatherInfo(
            curTemp = "25°C",
            tempMin = "20°C",
            tempMax = "30°C",
            description = "晴",
            city = "天津"
        )
    }

    fun Cursor.getStringOrNull(columnName: String): String? {
        val index = getColumnIndex(columnName)
        return if (index == -1) null else getString(index)
    }

    suspend fun getTianjinWeather2(): WeatherInfo = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("https://api.openweathermap.org/data/2.5/weather?q=Tianjin&appid=74405d40f6d04764d10386072bae9ace&units=metric")
            .build()

        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            val json = response.body?.string()
            val weatherResponse = gson.fromJson(json, WeatherResponse::class.java)

            val info = WeatherInfo(
                curTemp = "${weatherResponse.main.temp.toInt()}",
                tempMin = "${weatherResponse.main.temp_min.toInt()}°",
                tempMax = "${weatherResponse.main.temp_max.toInt()}°",
                description = weatherResponse.weather.firstOrNull()?.description ?: "未知天气",
                city = weatherResponse.name
            ).also { info ->
                //db.addWeather(info)
            }

            return@withContext info

        } else {
            throw Exception("Failed to load weather data from API")
        }
    }
}