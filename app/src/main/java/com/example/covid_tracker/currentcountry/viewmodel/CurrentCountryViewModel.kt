package com.example.covid_tracker.currentcountry.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid_tracker.countrydetails.model.CountryData
import com.example.covid_tracker.currentcountry.repository.CurrentCountryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrentCountryViewModel(
    private val repository: CurrentCountryRepository
) : ViewModel() {

    private val TAG = "CurrentCountryViewModel"

    private val _currentCountryData = MutableLiveData<CountryData>()
    val currentCountryDataLiveData: LiveData<CountryData> get() = _currentCountryData

    private val _errorGettingCountryName = MutableLiveData<Boolean>()
    val errorGettingCountryNameLiveData: LiveData<Boolean> get() = _errorGettingCountryName

    fun fetchDataForCords(lat: String, lon: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var errorOccurred = true
            var logMsg = "Cannot fetch data for cords"
            val countryNameResponse = repository.getNameForCords(lat, lon)

            if (countryNameResponse.isSuccessful) {
                countryNameResponse.body()?.let { geocodingModel ->
                    val countryCovidDataResponse = repository.getCountryCovidData(geocodingModel[0].country)

                    if (countryCovidDataResponse.isSuccessful) {
                        countryCovidDataResponse.body()?.let { countryData ->
                            _currentCountryData.postValue(countryData)
                            errorOccurred = false
                            logMsg = "Data for cords fetched"
                        }
                    }
                }
            }

            Log.d(TAG, logMsg)
            _errorGettingCountryName.postValue(errorOccurred)
        }
    }

}