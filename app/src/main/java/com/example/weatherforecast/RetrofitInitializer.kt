package com.example.weatherforecast

import android.app.Application
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer(application: Application) {

    private val okHttpClient = OkHttpClient.Builder()
        .cache(Cache(application.cacheDir, (5 * 1024 * 1024).toLong()))
        .addInterceptor { chain ->
            val request = chain.request()
            request.newBuilder().header("Cache-Control", "public, max-age=" + 7200).build()
            chain.proceed(request)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    fun weatherService() = retrofit.create(WeatherService::class.java)

}