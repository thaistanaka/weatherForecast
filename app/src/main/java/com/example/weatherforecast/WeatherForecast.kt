package com.example.weatherforecast

import com.google.gson.annotations.SerializedName

class WeatherForecast {

    @SerializedName("list")
    var list: List<ListWeather>? = null

    @SerializedName("city")
    var city: City? = null
}

class City {

    @SerializedName("name")
    var city_name: String? = null
}

class ListWeather {

    @SerializedName("main")
    var main: Main? = null

    @SerializedName("rain")
    var rain: Rain? = null

    @SerializedName("snow")
    var snow: Snow? = null

    @SerializedName("wind")
    var wind: Wind? = null

    @SerializedName("dt_txt")
    var date: String? = null
}

class Rain {

    @SerializedName("3h")
    var h3: Float = 0.toFloat()
}

class Snow {

    @SerializedName("3h")
    var h3: Float = 0.toFloat()
}

class Wind {

    @SerializedName("speed")
    var speed: Float = 0.toFloat()
}

class Main {

    @SerializedName("temp")
    var temp: Float = 0.toFloat()

    @SerializedName("feels_like")
    var feels_like: Float = 0.toFloat()

    @SerializedName("temp_min")
    var temp_min: Float = 0.toFloat()

    @SerializedName("temp_max")
    var temp_max: Float = 0.toFloat()

    @SerializedName("humidity")
    var humidity: Float = 0.toFloat()

}