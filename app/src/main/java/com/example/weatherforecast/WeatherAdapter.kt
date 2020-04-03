package com.example.weatherforecast

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {
    private var listWeather = emptyList<ListWeather>()

    inner class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val temp: TextView = itemView.findViewById(R.id.temp)
        val minTemp: TextView = itemView.findViewById(R.id.minTemp)
        val maxTemp: TextView = itemView.findViewById(R.id.maxTemp)
        val rain: TextView = itemView.findViewById(R.id.rain)
        val snow: TextView = itemView.findViewById(R.id.snow)
        val wind: TextView = itemView.findViewById(R.id.wind)
        val humidity: TextView = itemView.findViewById(R.id.humidity)
        val time: TextView = itemView.findViewById(R.id.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return WeatherViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather = listWeather[position]
        holder.temp.text = "Temp: " + weather.main?.temp.toString()
        holder.minTemp.text = "Temp Min: " + weather.main?.temp_min.toString()
        holder.maxTemp.text = "Temp Max: " + weather.main?.temp_max.toString()
        holder.humidity.text = "Humidity: " + weather.main?.humidity.toString()

        if (weather.rain?.h3 == null) {
            holder.rain.visibility = View.GONE
        } else {
            holder.rain.visibility = View.VISIBLE
            holder.rain.text = "Rain: " + weather.rain?.h3.toString()
        }

        if (weather.snow?.h3 == null) {
            holder.snow.visibility = View.GONE
        } else {
            holder.snow.visibility = View.VISIBLE
            holder.snow.text = "Snow: " + weather.snow?.h3.toString()
        }
        holder.wind.text = "Wind: " + weather.wind?.speed.toString()
        val dateTime = weather.date!!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ldt: LocalDateTime =
                LocalDateTime.parse(
                    dateTime,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                )
            holder.time.text =
                ldt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")).toString()
        } else {
            holder.time.text = dateTime
        }
    }

    fun setWeatherList(listWeather: List<ListWeather>) {
        this.listWeather = listWeather
        notifyDataSetChanged()
    }

    override fun getItemCount() = listWeather.size
}