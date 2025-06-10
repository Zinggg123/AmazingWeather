package com.bd.amazingweather.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bd.amazingweather.model.WeatherInfo
import com.bd.amazingweather.model.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {
    val _weatherInfo = MutableLiveData<WeatherInfo>()

    fun loadWeatherInfo() {
        viewModelScope.launch {
            val info = WeatherRepository.getTianjinWeather2()
            _weatherInfo.postValue(info)
        }
    }
}