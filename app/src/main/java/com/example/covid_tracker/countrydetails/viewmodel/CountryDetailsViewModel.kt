package com.example.covid_tracker.countrydetails.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid_tracker.countrydetails.model.CountryData
import com.example.covid_tracker.countrydetails.repository.CountryDetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CountryDetailsViewModel(
    private val repository: CountryDetailsRepository
) : ViewModel() {

    private val TAG = "CountryDetailsViewModel"

    private val _countryData = MutableLiveData<CountryData>()
    val countryData: LiveData<CountryData> get() = _countryData

    private val _countryDataFetchErrorOccurred = MutableLiveData<Boolean>()
    val countryDataFetchErrorOccurred: LiveData<Boolean> get() = _countryDataFetchErrorOccurred

    private val _fetchingData = MutableLiveData<Boolean>()
    val fetchingData: LiveData<Boolean> get() = _fetchingData

    fun fetchCountryData(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _fetchingData.postValue(true)

            var refreshResult = true
            var logMsg = "Cannot fetch country details"
            val response = repository.getCountryData(name)

            if (response.isSuccessful) {
                response.body()?.let {
                    _countryData.postValue(it)
                    refreshResult = false
                    logMsg = "Fetched country details"
                }
            }

            Log.d(TAG, logMsg)
            _fetchingData.postValue(false)
            _countryDataFetchErrorOccurred.postValue(refreshResult)
        }
    }

}