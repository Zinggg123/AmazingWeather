package com.bd.amazingweather

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class WeatherPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val cities = listOf("Tianjin", "Beijing", "Shanghai", "Guangzhou")

    override fun getItemCount(): Int = cities.size

    override fun createFragment(position: Int): Fragment {
        return WeatherFragment().apply {
            arguments = Bundle().apply {
                putString("city", cities[position])
            }
        }
    }

    fun getCityName(position: Int): String? {
        return if (position in 0 until itemCount) cities[position] else null
    }
}