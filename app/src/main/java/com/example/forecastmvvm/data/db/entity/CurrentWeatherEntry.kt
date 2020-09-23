package com.example.forecastmvvm.data.db.entity


import androidx.room.*
import com.example.forecastmvvm.data.db.converters.ListToStringConverter
import com.google.gson.annotations.SerializedName

const val CURRENT_WEATHER_ID = 0

@Entity(tableName = "current_weather")
data class CurrentWeatherEntry(
    @SerializedName("observation_time")
    var observationTime: String?,
    var temperature: Int?,
    @SerializedName("weather_code")
    var weatherCode: Int?,
    //@SerializedName("weather_icons")
    //@Embedded
    //var weatherIcons: List<String>?,
   // @SerializedName("weather_descriptions")
    //@TypeConverters(ListToStringConverter::class)
    //@Embedded
    //var weatherDescriptions: List<String>?,
    @SerializedName("wind_speed")
    var windSpeed: Int?,
    @SerializedName("wind_degree")
    var windDegree: Int?,
    @SerializedName("wind_dir")
    var windDir: String?,
    var pressure: Int?,
    var precip: Double?,
    var humidity: Int?,
    var cloudcover: Int?,
    var feelslike: Int?,
    @SerializedName("uv_index")
    var uvIndex: Int?,
    var visibility: Int?,
    @SerializedName("is_day")
    var isDay: String?
) {
    constructor() : this(null,null,null,null,null,null,null,
        null,null,null,null,null,
        null,null)

    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_WEATHER_ID
}