package com.example.forecastmvvm.data

import com.example.forecastmvvm.data.response.CurrentWeatherResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.logging.Level

const val API_KEY = "3dfb3d850a43735e3f8b9f00e7aca2ad"

// http://api.weatherstack.com/current?access_key=3dfb3d850a43735e3f8b9f00e7aca2ad&query=New%20York


interface ApixuWeatherApiService {

    @GET("current")
    fun getCurrentWeather(
        @Query("query") location: String//,
        //@Query("lang") lang: String = "en"
    ): Deferred<CurrentWeatherResponse>

    companion object{
        // прочитать про invoke
        operator fun invoke(): ApixuWeatherApiService{
            val requestInterceptor = Interceptor { chain ->
                    val url = chain.request()
                        .url()
                        .newBuilder()
                        .addQueryParameter("access_key", com.example.forecastmvvm.data.API_KEY)
                        .build()

                    val request = chain.request()
                        .newBuilder()
                        .url(url)
                        .build()

                return@Interceptor chain.proceed(request)
            }

            val log = HttpLoggingInterceptor()
            log.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(log)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://api.weatherstack.com/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApixuWeatherApiService::class.java)
        }
    }
}