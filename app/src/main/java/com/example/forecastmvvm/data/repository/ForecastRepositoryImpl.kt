package com.example.forecastmvvm.data.repository

import androidx.lifecycle.LiveData
import com.example.forecastmvvm.data.db.CurrentWeatherDao
import com.example.forecastmvvm.data.db.entity.CurrentWeatherEntry
import com.example.forecastmvvm.data.network.WeatherNetworkDataSource
import com.example.forecastmvvm.data.network.response.CurrentWeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class ForecastRepositoryImpl (
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource
) : ForecastRepository {
    //в инит блоке определяются обсерверы на livedata
    init {
        // репозиторий не имеет lifecycle, поэтому можно использовать observeForever
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever{
            newCurrentWeather -> persistFetchedCurrentWeather(newCurrentWeather)
        }
    }
    // save data in local storage
    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse){
        GlobalScope.launch(Dispatchers.IO) { // no lifecycle, so GlobalScope
            currentWeatherDao.insert(fetchedWeather.currentWeatherEntry)
        }
    }

    override suspend fun getCurrentWeather(): LiveData<CurrentWeatherEntry> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext currentWeatherDao.getWeather()
        }
    }

    private suspend fun initWeatherData(){
        // dummy. this expression is always true
        if (isFetchCurrentNeeded(ZonedDateTime.now().minusHours(1)))
            fetchCurrentWeather()
    }

    private suspend fun fetchCurrentWeather(){
        weatherNetworkDataSource.fetchCurrentWeather("Los Angeles")
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }
}