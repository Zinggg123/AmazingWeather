package com.bd.amazingweather.model

import com.bd.amazingweather.App
import kotlin.collections.mutableListOf

object CityRespository {
    private val dbHelper = DataBaseHelper(App.instance)

    suspend fun getCities(): MutableList<String>? {
        val cursor = dbHelper.getAllCities()
        val cityList = mutableListOf<String>()
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex("city")
            do {
                val cityName = cursor.getString(columnIndex)
                cityList.add(cityName)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return cityList
    }

    fun addCity(city: String){
        dbHelper.addCities(city)
    }
}