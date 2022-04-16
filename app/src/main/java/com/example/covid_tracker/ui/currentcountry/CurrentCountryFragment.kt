package com.example.covid_tracker.ui.currentcountry

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.covid_tracker.R
import com.example.covid_tracker.utils.LocationUtils.startLocationDataUpdate
import com.example.covid_tracker.utils.LocationUtils.stopLocationDataUpdate
import com.example.covid_tracker.databinding.CurrentCountryFragmentBinding
import com.example.covid_tracker.model.CountryData
import com.example.covid_tracker.utils.DialogCreator
import com.example.covid_tracker.utils.LOCATION_UPDATE_INTERVAL
import com.example.covid_tracker.utils.REQUEST_CODE_LOCATION_PERMISSION
import com.example.covid_tracker.utils.showSnackBar
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class CurrentCountryFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private val TAG = "CurrentCountryFragment"

    private var _binding: CurrentCountryFragmentBinding? = null
    private val binding get() = _binding!!
    private val currentCountryViewModel by viewModels<CurrentCountryViewModel>()
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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CurrentCountryFragmentBinding.inflate(inflater, container, false)

        requestPermissions()
        hideDataShowLoading()
        observeData()

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
        Log.d(TAG, "Fragment destroyed")
        _binding = null
    }

    private fun observeData() {
        currentCountryViewModel.currentCountryData.observe(viewLifecycleOwner, { data ->
            Log.d(TAG, "Got data for current city: $data")
            hideDataShowLoading()
            updateUI(data)
            hideLoadingShowData()
        })

        currentCountryViewModel.gettingCountryNameLiveSuccessful.observe(viewLifecycleOwner, {
            when (it) {
                false -> {
                    DialogCreator(
                        R.string.dialog_title_error,
                        R.string.dialog_message_cannot_get_country_name
                    ).showDialog(requireActivity())
                }
                true -> { showSnackBar(binding.root, getString(R.string.current_country_fragment_name_fetch_success)) }
            }
        })

        currentCountryViewModel.gettingCountryCovidDataSuccessful.observe(viewLifecycleOwner, {
            when (it) {
                false -> {
                    DialogCreator(
                        R.string.dialog_title_error,
                        R.string.dialog_message_cannot_fetch_data
                    ).showDialog(requireActivity())
                }
                true -> { showSnackBar(binding.root, getString(R.string.current_country_fragment_covid_data_fetch_success)) }
            }
        })

        currentCountryViewModel.exceptionAppeared.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    DialogCreator(
                        R.string.dialog_title_error,
                        R.string.dialog_message_exception_message,
                        R.drawable.dialog_exception_appeared
                    ).showDialog(requireActivity())
                }
                false -> { /* do nothing */ }
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

    private fun hideDataShowLoading() {
        binding.lCurrentCountryLocationInfo.visibility = View.GONE
        binding.iCurrentCountryTotalData.tvCurrentCountryTotalLabel.visibility = View.GONE
        binding.iCurrentCountryTotalData.glTotalData.visibility = View.GONE
        binding.iCurrentCountryTodayData.tvCurrentCountryTodayLabel.visibility = View.GONE
        binding.iCurrentCountryTodayData.glTodayData.visibility = View.GONE
        binding.pbCurrentCountryLoading.visibility = View.VISIBLE
    }

    private fun hideLoadingShowData() {
        binding.lCurrentCountryLocationInfo.visibility = View.VISIBLE
        binding.iCurrentCountryTotalData.tvCurrentCountryTotalLabel.visibility = View.VISIBLE
        binding.iCurrentCountryTotalData.glTotalData.visibility = View.VISIBLE
        binding.iCurrentCountryTodayData.tvCurrentCountryTodayLabel.visibility = View.VISIBLE
        binding.iCurrentCountryTodayData.glTodayData.visibility = View.VISIBLE
        binding.pbCurrentCountryLoading.visibility = View.GONE
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