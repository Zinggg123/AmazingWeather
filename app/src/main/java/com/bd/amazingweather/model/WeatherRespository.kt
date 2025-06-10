package com.bd.amazingweather.model

import android.database.Cursor
import com.bd.amazingweather.App
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

    private val dbHelper = DataBaseHelper(App.instance)

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

    suspend fun getWeather3(city: String): WeatherInfo = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=74405d40f6d04764d10386072bae9ace&units=metric")
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

    suspend fun addWeather(weatherInfo: WeatherInfo): Long {
        return withContext(Dispatchers.IO) {
            dbHelper.addWeather(weatherInfo)
        }
    }

    suspend fun getWeather(city: String): WeatherInfo? {
        return withContext(Dispatchers.IO) {
            val cursor = dbHelper.getWeather(city)
            cursor?.use {
                if (it.moveToFirst()) {
                    WeatherInfo(
                        it.getString(it.getColumnIndexOrThrow("curTemp")),
                        it.getString(it.getColumnIndexOrThrow("tempMin")),
                        it.getString(it.getColumnIndexOrThrow("tempMax")),
                        it.getString(it.getColumnIndexOrThrow("description")),
                        it.getString(it.getColumnIndexOrThrow("city"))
                    )
                } else null
            }
        }
    }

    suspend fun updateWeather(weatherInfo: WeatherInfo): Long {
        return withContext(Dispatchers.IO) {
            dbHelper.updateWeather(weatherInfo)
        }
    }

    suspend fun getWeather0(city:String):WeatherInfo?{
        var weatherInfo = getWeather(city)

        if(weatherInfo!=null){
            return weatherInfo
        }

        weatherInfo = getWeather3(city)
        updateWeather(weatherInfo)

        return weatherInfo
    }
}