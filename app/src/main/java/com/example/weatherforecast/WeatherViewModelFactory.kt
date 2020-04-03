package com.example.weatherforecast

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WeatherViewModelFactory(
    private val application: Application,
    private val lat: MutableLiveData<String>,
    private val lon: MutableLiveData<String>
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(application, lat, lon) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}