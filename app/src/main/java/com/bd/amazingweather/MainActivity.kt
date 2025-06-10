package com.bd.amazingweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bd.amazingweather.databinding.MainScreenBinding
import com.bd.amazingweather.ui.theme.AmazingWeatherTheme
import com.bd.amazingweather.viewModel.WeatherViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainScreenBinding
    private lateinit var viewModel: WeatherViewModel

//    private lateinit var pagerAdapter: WeatherPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.main_screen)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

//        viewModel.loadWeatherInfo()
        binding.viewPager.adapter = WeatherPagerAdapter(this)

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AmazingWeatherTheme {
        Greeting("Android")
    }
}