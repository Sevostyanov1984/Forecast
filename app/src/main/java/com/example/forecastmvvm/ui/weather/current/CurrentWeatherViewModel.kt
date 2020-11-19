package com.example.forecastmvvm.ui.weather.current

import androidx.lifecycle.ViewModel
import com.example.forecastmvvm.data.internal.UnitSystem
import com.example.forecastmvvm.data.internal.lazyDeferred
import com.example.forecastmvvm.data.repository.ForecastRepository

class CurrentWeatherViewModel( private val forecastRepository: ForecastRepository) : ViewModel() {

    private val unitSystem = UnitSystem.METRIC //get from settings later

    val isMetric:Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather()
    }
}
