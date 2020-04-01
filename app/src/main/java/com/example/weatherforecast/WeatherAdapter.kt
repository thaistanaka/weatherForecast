package com.example.weatherforecast

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WeatherAdapter (context: Context): RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {
    private val context = context
    private var listWeather = emptyList<ListWeather>()

    inner class WeatherViewHolder(itemView: View)  : RecyclerView.ViewHolder(itemView){
        val temp: TextView = itemView.findViewById(R.id.temp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return WeatherViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather = listWeather[position]
        holder.temp.text = weather.main?.temp.toString()
    }

    fun setWeatherList(listWeather: List<ListWeather>) {
        this.listWeather = listWeather
        notifyDataSetChanged()
    }

    override fun getItemCount() = listWeather.size
}