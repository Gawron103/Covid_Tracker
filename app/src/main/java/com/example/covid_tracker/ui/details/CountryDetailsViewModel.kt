package com.example.covid_tracker.ui.details

import android.util.Log
import androidx.lifecycle.*
import com.example.covid_tracker.network.CovidApiResponse
import com.example.covid_tracker.repository.CountryDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryDetailsViewModel @Inject constructor(
    private val repository: CountryDetailsRepository
) : ViewModel() {

    private val TAG = "CountryDetailsViewModel"

    private val _countryCovidData = MutableLiveData<CovidApiResponse>()
    val countryCovidData: LiveData<CovidApiResponse> get() = _countryCovidData

    private var getCountryCovidDataJob: Job? = null

    fun requestCountryCovidData(name: String) {
        getCountryCovidDataJob = viewModelScope.launch(Dispatchers.IO) {
            repository.getCountryData(name)
                .cancellable()
                .collect {
                    _countryCovidData.postValue(it)
                }
        }
    }

    override fun onCleared() {
        Log.d(TAG, "CountryDetailsViewModel onCleared")
        super.onCleared()
        getCountryCovidDataJob?.cancel()
    }

}