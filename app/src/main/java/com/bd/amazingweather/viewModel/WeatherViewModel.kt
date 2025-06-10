package com.bd.amazingweather.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bd.amazingweather.model.City
import com.bd.amazingweather.model.CityRespository
import com.bd.amazingweather.model.WeatherInfo
import com.bd.amazingweather.model.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {
    val _weatherInfo = MutableLiveData<WeatherInfo?>()

    private val _cityList = MutableLiveData<List<String>>()
    val cityList: LiveData<List<String>> get() = _cityList

    fun loadWeatherInfo() {
        viewModelScope.launch {
            val info = WeatherRepository.getTianjinWeather2()
            _weatherInfo.postValue(info)
        }
    }

    fun loadWeatherInfo2(city: String) {
        viewModelScope.launch {
            val info = WeatherRepository.getWeather3(city)
            _weatherInfo.postValue(info)
        }
    }

    fun loadWeatherInfo3(city: String) {
        viewModelScope.launch {
            val info = WeatherRepository.getWeather0(city)
            _weatherInfo.postValue(info)
        }
    }

    fun loadCityInfo(){
        viewModelScope.launch{
            val cities = CityRespository.getCities()?: emptyList()
            _cityList.postValue(cities)
        }
    }

    fun addCity(city: String){
        viewModelScope.launch {
            CityRespository.addCity(city)
        }
    }

}