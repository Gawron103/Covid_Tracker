package com.example.covid_tracker.currentcountry.view

import android.Manifest
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.covid_tracker.countrydetails.model.CountryData
import com.example.covid_tracker.countrydetails.repository.service.CountryApiService
import com.example.covid_tracker.currentcountry.repository.CurrentCountryRepository
import com.example.covid_tracker.currentcountry.repository.service.GeocodingApiService
import com.example.covid_tracker.currentcountry.utils.Constants.LOCATION_UPDATE_INTERVAL
import com.example.covid_tracker.currentcountry.utils.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.example.covid_tracker.currentcountry.utils.LocationUtils.startLocationDataUpdate
import com.example.covid_tracker.currentcountry.utils.LocationUtils.stopLocationDataUpdate
import com.example.covid_tracker.currentcountry.viewmodel.CurrentCountryViewModel
import com.example.covid_tracker.currentcountry.viewmodel.CurrentCountryViewModelFactory
import com.example.covid_tracker.databinding.CurrentCountryFragmentBinding
import com.google.android.gms.location.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class CurrentCountryFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private val TAG = "CurrentCountryFragment"

    private var _binding: CurrentCountryFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentCountryViewModel: CurrentCountryViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationCallback = object: LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation = locationResult.lastLocation
            currentCountryViewModel.fetchDataForCords(
                lastLocation.latitude.toString(),
                lastLocation.longitude.toString()
            )
            Log.d(TAG, "Location: $lastLocation")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CurrentCountryFragmentBinding.inflate(inflater, container, false)

        requestPermissions()
        observeData()
        showLoading()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        startLocationDataUpdate(
            fusedLocationClient,
            locationCallback,
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            LOCATION_UPDATE_INTERVAL
        )
    }

    override fun onPause() {
        super.onPause()
        stopLocationDataUpdate(
            fusedLocationClient,
            locationCallback
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupViewModel() {
        currentCountryViewModel = ViewModelProvider(
            requireActivity(),
            CurrentCountryViewModelFactory(
                CurrentCountryRepository(
                    GeocodingApiService.create(),
                    CountryApiService.create()
                )
            )
        ).get(CurrentCountryViewModel::class.java)
    }

    private fun observeData() {
        currentCountryViewModel.currentCountryDataLiveData.observe(viewLifecycleOwner, { data ->
            Log.d(TAG, "Got data for current city: $data")
            updateCountryInfo(data)
            hideLoading()
        })

        currentCountryViewModel.errorGettingCountryNameLiveData.observe(viewLifecycleOwner, {
            val msg = if (!it) "Country name loaded" else "Cannot get country name"
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        })
    }

    private fun updateCountryInfo(data: CountryData) {
        // country section
        val locationCordsText = "${"%.2f\u00B0".format(data.countryInfo.lat)}, ${"%.2f\u00B0".format(data.countryInfo.long)}"
        binding.tvLocationCoords.text = locationCordsText
        val locationNameText = "${data.continent}, ${data.country}"
        binding.tvCurrentCountryLocationName.text = locationNameText

        // tests section
        binding.totalData.tvCurrentCountryTotalTests.text = data.tests.toString()

        // total section
        binding.totalData.tvCurrentCountryTotalCases.text = data.cases.toString()
        binding.totalData.tvCurrentCountryTotalRecovered.text = data.recovered.toString()
        binding.totalData.tvCurrentCountryTotalDeaths.text = data.deaths.toString()

        // today section
        binding.todayData.tvCurrentCountryTodayNewCases.text = data.todayCases.toString()
        binding.todayData.tvCurrentCountryTodayRecovered.text = data.todayRecovered.toString()
        binding.todayData.tvCurrentCountryTodayDeaths.text = data.todayDeaths.toString()
    }

    private fun showLoading() {
        binding.pbCurrentCountryLoading.visibility = View.VISIBLE

        // location section
        binding.lLocationInfo.visibility = View.GONE

        // total tests section
        binding.totalData.cvTotalTests.visibility = View.GONE

        // total label
        binding.totalData.tvCurrentCountryTotalLabel.visibility = View.GONE

        // total grid layout
        binding.totalData.glTotalData.visibility = View.GONE

        // today label
        binding.todayData.tvCurrentCountryTodayLabel.visibility = View.GONE

        // today grid layout
        binding.todayData.glTodayData.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.pbCurrentCountryLoading.visibility = View.GONE

        // location section
        binding.lLocationInfo.visibility = View.VISIBLE

        // total tests section
        binding.totalData.cvTotalTests.visibility = View.VISIBLE

        // total label
        binding.totalData.tvCurrentCountryTotalLabel.visibility = View.VISIBLE

        // total grid layout
        binding.totalData.glTotalData.visibility = View.VISIBLE

        // today label
        binding.todayData.tvCurrentCountryTodayLabel.visibility = View.VISIBLE

        // today grid layout
        binding.todayData.glTodayData.visibility = View.VISIBLE
    }

    private fun hasLocationPermissions(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    private fun requestPermissions() {
        if (!hasLocationPermissions(requireContext())) {
            // no permissions
            EasyPermissions.requestPermissions(
                this,
                "Permissions needed to use this app",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) { /* do nothing */ }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
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