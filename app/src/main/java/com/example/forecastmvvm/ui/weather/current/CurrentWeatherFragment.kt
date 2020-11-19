package com.example.forecastmvvm.ui.weather.current

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

import com.example.forecastmvvm.R
import com.example.forecastmvvm.data.network.ApixuWeatherApiService
import com.example.forecastmvvm.data.network.ConnectivityInterceptorImpl
import com.example.forecastmvvm.data.network.WeatherNetworkDataSourceImpl
import com.example.forecastmvvm.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val ViewModelFactory: CurrentWeatherViewModelFactory by instance()

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, ViewModelFactory)
            .get(CurrentWeatherViewModel::class.java)

        bindUi()
    }

    // текущий фрагмент наследуется от ScopedFragment, который в свою очередь имплементирует CoroutineScope
    // соответственно можно сразу запускать launch (GlobalScope.launch во фрагментах делать нельзя из-за lifecycle)
    private fun bindUi() = launch{
        val currentWeather = viewModel.weather.await()
        currentWeather.observe(this@CurrentWeatherFragment, Observer {
            // если в бд нет записи, то textView.text = it.toString() вернет ошибку, поэтому просто заверщшаем функцию
            if (it == null) return@Observer
            textView.text = it.toString()
        })
    }
}
