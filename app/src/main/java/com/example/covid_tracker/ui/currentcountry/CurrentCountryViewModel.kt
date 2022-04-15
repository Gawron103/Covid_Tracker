package com.example.covid_tracker.ui.currentcountry

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid_tracker.model.CountryData
import com.example.covid_tracker.repository.CurrentCountryRepository
import kotlinx.coroutines.*

class CurrentCountryViewModel(
    private val repository: CurrentCountryRepository
) : ViewModel() {

    private val TAG = "CurrentCountryViewModel"

    private val _currentCountryData = MutableLiveData<CountryData>()
    val currentCountryData: LiveData<CountryData> get() = _currentCountryData

    private val _gettingCountryNameSuccessful = MutableLiveData<Boolean>()
    val gettingCountryNameLiveSuccessful: LiveData<Boolean> get() = _gettingCountryNameSuccessful

    private val _gettingCountryCovidDataSuccessful = MutableLiveData<Boolean>()
    val gettingCountryCovidDataSuccessful: LiveData<Boolean> get() = _gettingCountryCovidDataSuccessful

    private val _exceptionAppeared = MutableLiveData<Boolean>()
    val exceptionAppeared: LiveData<Boolean> get() = _exceptionAppeared

    private val coroutineExceptionHelper = CoroutineExceptionHandler { _, throwable ->
        onException(throwable.localizedMessage ?: "No message")
    }

    private var _fetchJob: Job? = null

    fun fetchDataForCords(lat: String, lon: String) {
        _exceptionAppeared.value = false

        _fetchJob = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHelper) {
            val countryNameResponse = repository.getNameForCords(lat, lon)

            if (countryNameResponse.isSuccessful) {
                countryNameResponse.body()?.let { geocodingModel ->
                    val countryCovidDataResponse =
                        repository.getCountryCovidData(geocodingModel[0].country)

                    if (countryCovidDataResponse.isSuccessful) {
                        countryCovidDataResponse.body()?.let { countryData ->
                            withContext(Dispatchers.Main) {
                                _currentCountryData.value = countryData
                            }
                        }
                    } else { onFetchCountryCovidDataError(countryCovidDataResponse.message()) }
                }
            } else { onFetchCountryNameError(countryNameResponse.message()) }
        }
    }

    override fun onCleared() {
        Log.d(TAG, "CurrentCountryViewModel onCleared")
        super.onCleared()
        _fetchJob?.cancel()
    }

    private fun onException(message: String) {
        Log.d(TAG, "Error fetching data: $message")
        _exceptionAppeared.postValue(true)
    }

    private fun onFetchCountryNameError(message: String) {
        Log.d(TAG, "Error getting country name: $message")
        _gettingCountryNameSuccessful.postValue(false)
    }

    private fun onFetchCountryCovidDataError(message: String) {
        Log.d(TAG, "Error getting country Covid data: $message")
        _gettingCountryCovidDataSuccessful.postValue(false)
    }

}