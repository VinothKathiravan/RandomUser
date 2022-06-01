package com.zoho.randomuser

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.kwabenaberko.openweathermaplib.constant.Units
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper
import com.kwabenaberko.openweathermaplib.implementation.callback.CurrentWeatherCallback
import com.kwabenaberko.openweathermaplib.model.currentweather.CurrentWeather
import com.zoho.randomuser.databinding.FragmentUserDetailBinding

class UserDetailsFragment : Fragment() {

    private val args: UserDetailsFragmentArgs by navArgs()
    private lateinit var openWeatherMapHelper: OpenWeatherMapHelper
    private lateinit var binding: FragmentUserDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        context ?: return binding.root

        openWeatherMapHelper = OpenWeatherMapHelper(getString(R.string.weather_app_api_key))
        openWeatherMapHelper.setUnits(Units.METRIC)

        binding.name = args.userName
        binding.email = args.userEmail
        binding.phone = args.userPhone
        binding.address = args.userAddress
        binding.profileUrl = args.userPictureUrl
        binding.gender = args.userGender

        if (args.latitude.isNotEmpty() && args.longitude.isNotEmpty()) {
            getWeatherUsingLocation(args.latitude.toDouble(), args.longitude.toDouble(), R.string.weather_app_image_icon_url)
        }

        binding.setBackClickListener {
            it.findNavController().navigateUp()
        }

        return binding.root

    }

    private fun getWeatherUsingLocation(lat: Double, long: Double, @StringRes res: Int) {
        openWeatherMapHelper.getCurrentWeatherByGeoCoordinates(lat, long, object :
            CurrentWeatherCallback {
            override fun onSuccess(currentWeather: CurrentWeather) {
                binding.weather = "${currentWeather.main.temp}° ${currentWeather.weather[0].description}"
                binding.weatherIconUrl = getString(res, currentWeather.weather[0].icon)
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("ERROR", throwable.message!!)
            }
        })
    }
}