package com.zoho.randomuser

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kwabenaberko.openweathermaplib.constant.Units
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper
import com.kwabenaberko.openweathermaplib.implementation.callback.CurrentWeatherCallback
import com.kwabenaberko.openweathermaplib.model.currentweather.CurrentWeather
import com.zoho.randomuser.adapter.RandomUserAdapter
import com.zoho.randomuser.databinding.FragmentRandomUserBinding
import com.zoho.randomuser.viewmodel.RandomUserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


const val LOCATION_REQUEST_CODE = 1001


@AndroidEntryPoint
class RandomUserFragment : Fragment() {

    private var requestJob: Job? = null
    private val adapter = RandomUserAdapter()
    private val viewModel: RandomUserViewModel by viewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var openWeatherMapHelper: OpenWeatherMapHelper

    private lateinit var binding: FragmentRandomUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRandomUserBinding.inflate(inflater, container, false)
        context ?: return binding.root

        binding.usersRecyclerView.adapter = adapter

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        openWeatherMapHelper = OpenWeatherMapHelper(getString(R.string.weather_app_api_key))
        openWeatherMapHelper.setUnits(Units.METRIC)

        subscribeUi(adapter)

        searching(binding.userSearchView)

        getUserLocation()

        return binding.root
    }

    private fun searching(search: SearchView) {
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.searchUserByQuery(newText)
                return true
            }
        })
    }

    private fun getUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            getWeatherUsingLocation(it)
        }
    }

    private fun getWeatherUsingLocation(location: Location) {
        openWeatherMapHelper.getCurrentWeatherByGeoCoordinates(location.latitude, location.longitude, object : CurrentWeatherCallback {
            override fun onSuccess(currentWeather: CurrentWeather) {
                binding.weather = "${currentWeather.main.temp}° ${currentWeather.name}"
                binding.weatherDescription = currentWeather.weather[0].description
                binding.weatherIconUrl = getString(R.string.weather_app_image_icon_url, currentWeather.weather[0].icon)
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("ERROR", throwable.message!!)
            }
        })
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_REQUEST_CODE
        )
    }

    private fun subscribeUi(adapter: RandomUserAdapter) {
        requestJob?.cancel()
        requestJob = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.randomUserList.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST_CODE -> getUserLocation()
        }
    }
}