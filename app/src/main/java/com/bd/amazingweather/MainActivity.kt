package com.bd.amazingweather

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bd.amazingweather.databinding.MainScreenBinding
import com.bd.amazingweather.ui.theme.AmazingWeatherTheme
import com.bd.amazingweather.viewModel.WeatherViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainScreenBinding
    private lateinit var viewModel: WeatherViewModel
    private lateinit var pagerAdapter: WeatherPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.main_screen)
        pagerAdapter = WeatherPagerAdapter(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.viewPager.adapter = pagerAdapter

//        viewModel.cityList.observe(this, Observer { cityList ->
//            if (cityList != null) {
//                pagerAdapter.updateCities(cityList)
//            }
//        })
//        viewModel.loadCityInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_city -> {
                showAddCityDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun showAddCityDialog() {
        val input = EditText(this).apply {
            hint = "Enter city name"
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            setHintTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
            background = getRoundedRectDrawable(
                ContextCompat.getColor(context, android.R.color.white),
                0f,
                1f,
                ContextCompat.getColor(context, R.color.white)
            )
            setTextSize(16f)
            setPadding(dpToPx(16f), dpToPx(8f), dpToPx(16f), dpToPx(8f))
        }

        val builder = AlertDialog.Builder(this)
            .setTitle("Add New City")
            .setView(input)
            .setPositiveButton("OK") { dialog, which ->
                val newCity = input.text.toString().trim()
                if (newCity.isNotEmpty()) {
                    addCityToList(newCity)
                }
            }
            .setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

        builder.show()
    }

    fun getRoundedRectDrawable(
        backgroundColor: Int,
        radiusDp: Float,
        strokeWidthDp: Float = 0f,
        strokeColor: Int = 0
    ): Drawable {
        val radius = dpToPx(radiusDp)
        val strokeWidth = dpToPx(strokeWidthDp)

        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = radius.toFloat()
        shape.setColor(backgroundColor)
        if (strokeWidth > 0) {
            shape.setStroke(strokeWidth.toInt(), strokeColor)
        }
        return shape
    }

    fun dpToPx(dp: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    private fun addCityToList(cityName: String) {
        val adapter = WeatherPagerAdapter(this)
        adapter.addCity(cityName)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AmazingWeatherTheme {
    }
}