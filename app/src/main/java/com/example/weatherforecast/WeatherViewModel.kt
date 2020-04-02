package com.example.weatherforecast

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class WeatherViewModel(application: Application, lat: MutableLiveData<String>, lon: MutableLiveData<String>):
    AndroidViewModel(application) {
    val weatherForecast = MutableLiveData<WeatherForecast>()
    val api = "35caeb2864260ad004cd15422eecd232"
    val unit = "metric"

    init {
        val latitude: String = lat.value.toString()
        val longitude: String = lon.value.toString()

        val call =
            RetrofitInitializer(application).weatherService().getCurrentWeatherData(latitude, longitude, api, unit)
        call.enqueue(object: Callback<WeatherForecast> {
            override fun onFailure(call: Call<WeatherForecast>, t: Throwable) {
            }

            override fun onResponse(call: Call<WeatherForecast>, response: Response<WeatherForecast>) {
                if(response.code() == 200){
                    updateWeather(response.body())
                }
            }
        })
    }

    fun updateWeather(weather: WeatherForecast?) {
        weatherForecast.value = weather
    }
}