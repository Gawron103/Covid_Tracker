package com.example.covid_tracker.ui.currentcountry

import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.covid_tracker.R
import com.example.covid_tracker.databinding.CurrentCountryFragmentBinding
import com.example.covid_tracker.model.CountryData
import com.example.covid_tracker.network.CovidApiStatus
import com.example.covid_tracker.utils.DialogCreator
import com.example.covid_tracker.utils.REQUEST_CODE_LOCATION_PERMISSION
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class CurrentCountryFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private val TAG = "CurrentCountryFragment"

    private var _binding: CurrentCountryFragmentBinding? = null
    private val binding get() = _binding!!

    private val currentCountryViewModel by viewModels<CurrentCountryViewModel>()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    private lateinit var geocoder: Geocoder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        geocoder = Geocoder(requireContext())
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(60)
            fastestInterval = TimeUnit.SECONDS.toMillis(5)
            maxWaitTime = TimeUnit.SECONDS.toMillis(90)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationCallback = object: LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                val countryCode = geocoder.getFromLocation(
                    locationResult.lastLocation.latitude,
                    locationResult.lastLocation.longitude,
                    1).first().countryCode
                currentCountryViewModel.requestCovidData(countryCode)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CurrentCountryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showLoadingOrData(true)

        if (hasLocationPermissions()) {
            observeCountryCovidData()
        } else {
            requestLocationPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "Binding reference set to null")
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Fragment destroyed")
    }

    private fun observeCountryCovidData() {
        currentCountryViewModel.data.observe(viewLifecycleOwner, { result ->
            result?.let {
                when (result.status) {
                    CovidApiStatus.SUCCESS -> {
                        result.data?.let { updateUI(it) }
                        showLoadingOrData(false)
                    }
                    CovidApiStatus.LOADING -> {
                        showLoadingOrData(true)
                    }
                    CovidApiStatus.ERROR -> {
                        Log.d(TAG, "Error: ${result.message}")
                        DialogCreator(
                            R.string.dialog_title_error,
                            R.string.dialog_message_cannot_fetch_data
                        ).showDialog(requireActivity())
                    }
                }
            }
        })
    }

    private fun updateUI(currentCountryData: CountryData) {
        // Country info section
        val locationCordsText = "${"%.2f\u00B0".format(currentCountryData.countryInfo.lat)}, ${"%.2f\u00B0".format(currentCountryData.countryInfo.long)}"
        binding.tvCurrentCountryCoords.text = locationCordsText
        val locationNameText = "${currentCountryData.continent}, ${currentCountryData.country}"
        binding.tvCurrentCountryLocationName.text = locationNameText

        // Total data section
        binding.iCurrentCountryTotalData.tvCurrentCountryTotalRecovered.text = currentCountryData.recovered.toString()
        binding.iCurrentCountryTotalData.tvCurrentCountryTotalDeaths.text = currentCountryData.deaths.toString()
        binding.iCurrentCountryTotalData.tvCurrentCountryTotalCases.text = currentCountryData.cases.toString()
        binding.iCurrentCountryTotalData.tvCurrentCountryTotalTests.text = currentCountryData.tests.toString()

        // Today data section
        binding.iCurrentCountryTodayData.tvCurrentCountryTodayRecovered.text = currentCountryData.todayRecovered.toString()
        binding.iCurrentCountryTodayData.tvCurrentCountryTodayDeaths.text = currentCountryData.todayDeaths.toString()
        binding.iCurrentCountryTodayData.tvCurrentCountryTodayNewCases.text = currentCountryData.todayCases.toString()
    }

    private fun showLoadingOrData(isLoading: Boolean) {
        if (isLoading) {
            binding.lCurrentCountryLocationInfo.visibility = View.GONE
            binding.iCurrentCountryTotalData.tvCurrentCountryTotalLabel.visibility = View.GONE
            binding.iCurrentCountryTotalData.glTotalData.visibility = View.GONE
            binding.iCurrentCountryTodayData.tvCurrentCountryTodayLabel.visibility = View.GONE
            binding.iCurrentCountryTodayData.glTodayData.visibility = View.GONE
            binding.pbCurrentCountryLoading.visibility = View.VISIBLE
        } else {
            binding.lCurrentCountryLocationInfo.visibility = View.VISIBLE
            binding.iCurrentCountryTotalData.tvCurrentCountryTotalLabel.visibility = View.VISIBLE
            binding.iCurrentCountryTotalData.glTotalData.visibility = View.VISIBLE
            binding.iCurrentCountryTodayData.tvCurrentCountryTodayLabel.visibility = View.VISIBLE
            binding.iCurrentCountryTodayData.glTodayData.visibility = View.VISIBLE
            binding.pbCurrentCountryLoading.visibility = View.GONE
        }
    }

    private fun hasLocationPermissions() =
        EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    private fun requestLocationPermissions() {
        EasyPermissions.requestPermissions(
            this,
            "Permissions needed to use this app",
            REQUEST_CODE_LOCATION_PERMISSION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) { /* do nothing */ }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestLocationPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}