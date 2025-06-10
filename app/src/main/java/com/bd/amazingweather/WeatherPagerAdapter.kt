package com.bd.amazingweather

import android.os.Bundle
import android.util.Log
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bd.amazingweather.viewModel.WeatherViewModel

class WeatherPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private lateinit var viewModel: WeatherViewModel
//    private var cities:List<String> = emptyList()
    private var cities = mutableListOf("Tianjin", "Beijing", "Shanghai", "Guangzhou")

    override fun getItemCount(): Int = cities.size

    override fun createFragment(position: Int): Fragment {
        Log.d("WeatherPagerAdapter", "Creating fragment for position $position")
        return WeatherFragment().apply {
            arguments = Bundle().apply {
                putString("city", cities[position])
            }
        }
    }

//    fun updateCities(newcs: List<String>){
//        cities = newcs
//        notifyDataSetChanged()
//    }

    fun getCityName(position: Int): String? {
        return if (position in 0 until itemCount) cities[position] else null
    }

    fun addCity(cityName: String) {
        viewModel.addCity(cityName)
        notifyDataSetChanged()
    }
}