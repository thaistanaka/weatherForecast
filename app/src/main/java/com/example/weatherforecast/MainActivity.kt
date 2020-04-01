package com.example.weatherforecast

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var adapter: WeatherAdapter
    lateinit var viewModel: WeatherViewModel
    lateinit var locationManager: LocationManager
    private var hasGps = false
    private var hasNetwork = false
    private var locationGps: Location? = null
    private var locationNetwork: Location? = null
    var lat = MutableLiveData<String>()
    var lon = MutableLiveData<String>()
    val PERMISSION_ID = 42

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getLocation()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        adapter = WeatherAdapter(this)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        val itemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.recyclerview.addItemDecoration(itemDecoration)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("MissingPermission")
    fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if(checkPermissions()) {
            if(hasGps || hasNetwork) {
                if(hasGps) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        10800000, 5000F, object : LocationListener {
                        override fun onLocationChanged(location: Location?) {
                            if(location != null){
                                locationGps = location
                                updateLocation(locationGps?.latitude.toString(),
                                    locationGps?.longitude.toString())
                            }
                        }

                        override fun onStatusChanged(
                            provider: String?,
                            status: Int,
                            extras: Bundle?
                        ) {
                        }

                        override fun onProviderEnabled(provider: String?) {
                        }

                        override fun onProviderDisabled(provider: String?) {
                        }
                    })

                    val localGpsLocation =
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (localGpsLocation != null) {
                        locationGps = localGpsLocation
                    }

                }

                if(hasNetwork) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        10800000, 5000F, object : LocationListener {
                        override fun onLocationChanged(location: Location?) {
                            if(location != null){
                                locationNetwork = location
                                updateLocation(locationNetwork?.latitude.toString(),
                                    locationNetwork?.longitude.toString())
                            }
                        }

                        override fun onStatusChanged(
                            provider: String?,
                            status: Int,
                            extras: Bundle?
                        ) {
                        }

                        override fun onProviderEnabled(provider: String?) {
                        }

                        override fun onProviderDisabled(provider: String?) {
                        }
                    })

                    val localNetworkLocation =
                        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    if (localNetworkLocation != null) {
                        locationNetwork = localNetworkLocation
                    }
                }
                if (locationGps != null && locationNetwork!=null){
                    if(locationGps!!.accuracy > locationNetwork!!.accuracy){
                        updateLocation(locationNetwork?.latitude.toString(),
                            locationNetwork?.longitude.toString())
                    } else {
                        updateLocation(locationGps?.latitude.toString(),
                            locationGps?.longitude.toString())
                    }
                }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    fun updateLocation(latitude: String, longitude: String) {
        lat.value = latitude
        lon.value = longitude

        val application = requireNotNull(this).application
        val viewModelFactory = WeatherViewModelFactory(application, lat, lon)
        viewModel = ViewModelProvider(this, viewModelFactory).get(WeatherViewModel::class.java)

        viewModel.weatherForecast.observe(this, Observer { weather ->
            weather?.let {
                it.list?.let { list -> adapter.setWeatherList(list) }
                binding.city.text = it.city?.city_name.toString()
            }
        })

        binding.weatherViewModel = viewModel
    }

    private fun checkPermissions(): Boolean {
        if ((ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) && ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) ==
            PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION),
            PERMISSION_ID
        )
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }
}
